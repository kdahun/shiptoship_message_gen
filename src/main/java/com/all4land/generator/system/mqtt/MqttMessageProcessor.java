package com.all4land.generator.system.mqtt;

import org.quartz.Scheduler;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.system.component.TimeMapRangeCompnents;
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.netty.dto.CreateMmsiRequest;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.all4land.generator.system.netty.dto.AsmControlMessage.AsmShipControl;
import com.all4land.generator.system.netty.dto.AsmControlMessage;
import com.all4land.generator.system.netty.dto.TsqResourceRequestMessage;
import com.all4land.generator.ui.tab.ais.model.TcpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpServerTableModel;
import com.all4land.generator.ais.ESIMessageUtil;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.ui.tab.ais.entity.TcpServerTableEntity;
import com.all4land.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;
import com.all4land.generator.ui.tab.ais.entity.UdpServerTableEntity;
import com.all4land.generator.system.netty.send.config.NettyServerTCPConfiguration;
import com.all4land.generator.system.netty.send.config.NettyServerUDPConfiguration;
import io.netty.channel.Channel;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * MQTT 메시지를 처리하는 클래스
 * TCP 서버와 동일한 방식으로 메시지를 처리합니다.
 */
public class MqttMessageProcessor implements MqttMessageCallback {
	
	private final GlobalEntityManager globalEntityManager;
	private final Scheduler scheduler;
	private final QuartzCoreService quartzCoreService;
	private final VirtualTimeManager virtualTimeManager;
	private final ApplicationEventPublisher eventPublisher;
	private final TcpServerTableModel tcpServerTableModel;
	private final UdpServerTableModel udpServerTableModel;
	private final TimeMapRangeCompnents timeMapRangeCompnents;
	private final Gson gson = new Gson();
	private final AtomicInteger tsqSeq = new AtomicInteger(1);
	
	public MqttMessageProcessor(GlobalEntityManager globalEntityManager,
			Scheduler scheduler,
			QuartzCoreService quartzCoreService,
			VirtualTimeManager virtualTimeManager,
			ApplicationEventPublisher eventPublisher,
			TcpServerTableModel tcpServerTableModel,
			UdpServerTableModel udpServerTableModel,
			TimeMapRangeCompnents timeMapRangeCompnents) {
		this.globalEntityManager = globalEntityManager;
		this.scheduler = scheduler;
		this.quartzCoreService = quartzCoreService;
		this.virtualTimeManager = virtualTimeManager;
		this.eventPublisher = eventPublisher;
		this.tcpServerTableModel = tcpServerTableModel;
		this.udpServerTableModel = udpServerTableModel;
		this.timeMapRangeCompnents = timeMapRangeCompnents;
	}
	
	@Override
	public void onMessage(String topic, String message) {
		System.out.println("[DEBUG] ========== MQTT 메시지 수신 ==========");
		System.out.println("[DEBUG] 토픽: " + topic);
		System.out.println("[DEBUG] 메시지: " + message);
		
		// 토픽에서 정보 추출
		// 형식 1: {송신 식별자}/{수신 식별자}/{카테고리}/{액션}/{timestamp}
		//   예시: mt/mg/traffic-ships/create/20260116112354
		// 형식 2: mg/ms/tsq/{mmsi}/{timestamp}/
		//   예시: mg/ms/tsq/440115678/20260122123456.1234/
		String normalizedTopic = topic.startsWith("/") ? topic.substring(1) : topic;
		String[] topicParts = normalizedTopic.split("/");
		
		String senderId = null;
		String receiverId = null;
		String category = null;
		String action = null;
		String timestamp = null;
		String mmsi = null;
		
		// 새로운 TSQ 토픽 형식 체크: mg/ms/tsq/{mmsi}/{timestamp}/
		if (topicParts.length >= 4 && "mg".equals(topicParts[0]) && "ms".equals(topicParts[1]) && "tsq".equals(topicParts[2])) {
			mmsi = topicParts[3];
			timestamp = topicParts.length >= 5 ? topicParts[4] : null;
			action = "tsq";
			
			System.out.println("[DEBUG] TSQ 토픽 형식 감지:");
			System.out.println("[DEBUG]   - MMSI: " + mmsi);
			System.out.println("[DEBUG]   - Timestamp: " + timestamp);
			System.out.println("[DEBUG]   - 액션: " + action);
		} else if (topicParts.length >= 5) {
			// 기존 형식: split("/") 결과: ["mt", "mg", "traffic-ships", "create", "20260116112354"]
			int offset = topicParts[0].isEmpty() ? 1 : 0; // 앞에 /가 있으면 offset=1
			senderId = topicParts[offset];         // 송신 식별자 (예: mt)
			receiverId = topicParts[offset + 1];  // 수신 식별자 (예: mg)
			category = topicParts[offset + 2];    // 카테고리 (예: traffic-ships)
			action = topicParts[offset + 3];      // 액션 (예: create, ais-state)
			timestamp = topicParts[offset + 4];    // timestamp (예: 20260116112354)
			
			System.out.println("[DEBUG] 토픽 정보 추출:");
			System.out.println("[DEBUG]   - 송신 식별자: " + senderId);
			System.out.println("[DEBUG]   - 수신 식별자: " + receiverId);
			System.out.println("[DEBUG]   - 카테고리: " + category);
			System.out.println("[DEBUG]   - 액션: " + action);
			System.out.println("[DEBUG]   - Timestamp: " + timestamp);
		} else {
			System.out.println("[DEBUG] ⚠️ 토픽 형식이 예상과 다릅니다.");
		}
		
		try {
			// 토픽의 액션에 따라 다른 처리
			if (action != null) {
				switch (action) {
					case "create":
						// 선박 생성: traffic-ships/create
						processCreateMessage(message);
						break;
					case "ais-state":
						// AIS 상태 제어: traffic-ships/ais-state
						processAisStateMessage(message);
						break;
					case "asm-state":
						// ASM 상태 제어: traffic-ships/asm-state
						processAsmStateMessage(message);
						break;
					case "tsq":
						// TSQ 메시지 전송: mg/ms/tsq/{mmsi}/{timestamp}/
						processTsqMessage(message, mmsi);
						break;
					case "sim-state":
						// 시뮬레이터 배속 제어: simulator/sim-state
						processSimStateMessage(message);
						break;
					default:
						// 기타 액션은 기본 JSON 처리
						processJsonMessage(message);
						break;
				}
			} else {
				// 액션을 추출할 수 없으면 기본 처리
				if (message.trim().startsWith("$STG-02")) {
					processNmeaControlMessage(message);
				} else {
					processJsonMessage(message);
				}
			}
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ MQTT 메시지 처리 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * JSON 메시지를 파싱하고 타입에 따라 처리합니다.
	 * 
	 * 지원하는 형식:
	 * 1. 객체 형식: {"type":"CREATE_MMSI", "data":[...]}
	 * 2. 배열 형식: [{mmsi:"...", lat:..., lon:..., ...}, ...]
	 */
	private void processJsonMessage(String jsonMessage) {
		try {
			String trimmedMessage = jsonMessage.trim();
			
			// 배열 형식인지 확인 (첫 문자가 '[')
			if (trimmedMessage.startsWith("[")) {
				// 배열 형식: 직접 MmsiData 배열로 파싱
				System.out.println("[DEBUG] 배열 형식 JSON 메시지 감지");
				handleArrayFormat(trimmedMessage);
			} else if (trimmedMessage.startsWith("{")) {
				// 객체 형식: CreateMmsiRequest로 파싱
				System.out.println("[DEBUG] 객체 형식 JSON 메시지 감지");
				CreateMmsiRequest request = gson.fromJson(trimmedMessage, CreateMmsiRequest.class);
				
				if (request == null || request.getType() == null) {
					System.out.println("[DEBUG] ⚠️ 유효하지 않은 JSON 메시지: " + jsonMessage);
					return;
				}
				
				System.out.println("[DEBUG] JSON 메시지 타입: " + request.getType());
				
				switch (request.getType()) {
					case "CREATE_MMSI":
						handleCreateMmsi(request);
						break;
					default:
						System.out.println("[DEBUG] ⚠️ 알 수 없는 메시지 타입: " + request.getType());
				}
			} else {
				System.out.println("[DEBUG] ⚠️ 유효하지 않은 JSON 형식: " + jsonMessage);
			}
		} catch (JsonSyntaxException e) {
			System.out.println("[DEBUG] ❌ JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 배열 형식의 JSON 메시지를 처리합니다.
	 * 형식: [{"mmsi":"...", "lat":..., "lon":..., "aisPeriod":...}, ...]
	 */
	private void handleArrayFormat(String jsonMessage) {
		try {
			// 배열을 MmsiData 리스트로 파싱
			java.lang.reflect.Type listType = new TypeToken<List<CreateMmsiRequest.MmsiData>>(){}.getType();
			List<CreateMmsiRequest.MmsiData> mmsiDataList = gson.fromJson(jsonMessage, listType);
			
			if (mmsiDataList == null || mmsiDataList.isEmpty()) {
				System.out.println("[DEBUG] ⚠️ 배열에 데이터가 없습니다.");
				return;
			}
			
			System.out.println("[DEBUG] 배열에서 " + mmsiDataList.size() + "개의 선박 데이터 발견");
			
			// CreateMmsiRequest 객체로 변환하여 기존 로직 재사용
			CreateMmsiRequest request = new CreateMmsiRequest();
			request.setType("CREATE_MMSI");
			request.setData(mmsiDataList);
			
			handleCreateMmsi(request);
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ 배열 형식 JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * CREATE_MMSI 타입 메시지를 처리하여 선박을 생성합니다.
	 */
	private void handleCreateMmsi(CreateMmsiRequest request) {
		if (request.getData() == null || request.getData().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ CREATE_MMSI 메시지에 데이터가 없습니다.");
			return;
		}
		
		int successCount = 0;
		int skipCount = 0;
		int errorCount = 0;
		
		for (CreateMmsiRequest.MmsiData mmsiData : request.getData()) {
			try {
				System.out.println("[DEBUG] 선박 생성 시도 - MMSI: " + mmsiData.getMmsi() + 
						", Lat: " + mmsiData.getLat() + ", Lon: " + mmsiData.getLon());
				
				globalEntityManager.createMmsiFromJson(
					scheduler,
					quartzCoreService,
					mmsiData.getMmsi(),
					mmsiData.getLat(),
					mmsiData.getLon(),
					mmsiData.getAisPeriod(),
					mmsiData.getRegionId()
				);
				
				successCount++;
				System.out.println("[DEBUG] ✅ 선박 생성 성공 - MMSI: " + mmsiData.getMmsi());
			} catch (IllegalArgumentException e) {
				// 중복된 MMSI 등으로 인한 스킵
				skipCount++;
				System.out.println("[DEBUG] ⚠️ 선박 생성 스킵 - MMSI: " + mmsiData.getMmsi() + 
						", 이유: " + e.getMessage());
			} catch (Exception e) {
				errorCount++;
				System.out.println("[DEBUG] ❌ 선박 생성 실패 - MMSI: " + mmsiData.getMmsi() + 
						", 오류: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		System.out.println("[DEBUG] ========== 선박 생성 완료 ==========");
		System.out.println("[DEBUG] 성공: " + successCount + ", 스킵: " + skipCount + ", 실패: " + errorCount);
	}
	
	/**
	 * 선박 생성 메시지를 처리합니다.
	 * 토픽: mt/mg/traffic-ships/create/{timestamp}
	 */
	private void processCreateMessage(String message) {
		System.out.println("[DEBUG] ========== 선박 생성 메시지 처리 ==========");
		processJsonMessage(message);
	}
	
	/**
	 * AIS 상태 제어 메시지를 처리합니다.
	 * 토픽: mt/mg/traffic-ships/ais-state/{timestamp}
	 */
	private void processAisStateMessage(String message) {
		System.out.println("[DEBUG] ========== AIS 상태 제어 메시지 처리 ==========");
		
		// NMEA 스타일 메시지 체크 ($STG-02로 시작)
		if (message.trim().startsWith("$STG-02")) {
			processNmeaControlMessage(message);
		} else {
			// JSON 형식의 제어 메시지 처리
			// 지원 형식:
			// 1. 객체 형식: {"ships": [{"mmsi": "440301234", "state": "1"}, ...]}
			// 2. 배열 형식: [{"mmsi": "440301234", "state": "1"}, ...]
			try {
				String trimmedMessage = message.trim();
				com.all4land.generator.system.netty.dto.ControlMessage controlMessage;
				
				if (trimmedMessage.startsWith("[")) {
					// 배열 형식: [{"mmsi": "...", "state": "..."}, ...]
					System.out.println("[DEBUG] 배열 형식 AIS 상태 제어 메시지 감지");
					java.lang.reflect.Type listType = new TypeToken<List<com.all4land.generator.system.netty.dto.ControlMessage.ShipControl>>(){}.getType();
					List<com.all4land.generator.system.netty.dto.ControlMessage.ShipControl> ships = gson.fromJson(trimmedMessage, listType);
					
					// ControlMessage 객체로 변환
					controlMessage = new com.all4land.generator.system.netty.dto.ControlMessage();
					controlMessage.setShips(ships);
				} else {
					// 객체 형식: {"ships": [...]}
					controlMessage = gson.fromJson(trimmedMessage, com.all4land.generator.system.netty.dto.ControlMessage.class);
				}
				
				if (controlMessage != null && controlMessage.getShips() != null) {
					handleControlMessage(controlMessage);
				} else {
					System.out.println("[DEBUG] ⚠️ AIS 상태 제어 메시지 형식이 올바르지 않습니다.");
				}
			} catch (JsonSyntaxException e) {
				System.out.println("[DEBUG] ❌ AIS 상태 제어 메시지 JSON 파싱 오류: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 제어 메시지를 처리하여 MMSI의 AIS 메시지 생성 상태를 변경합니다.
	 */
	private void handleControlMessage(com.all4land.generator.system.netty.dto.ControlMessage controlMessage) {
		if (controlMessage.getShips() == null || controlMessage.getShips().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ 제어 메시지에 ships 데이터가 없습니다.");
			return;
		}
		
		int successCount = 0;
		int failCount = 0;
		
		for (com.all4land.generator.system.netty.dto.ControlMessage.ShipControl ship : controlMessage.getShips()) {
			try {
				long mmsi = Long.parseLong(ship.getMmsi());
				String state = ship.getState(); // "0"=OFF, "1"=ON
				
				// destMMSI 리스트 추출 및 변환
				List<Long> destMMSIList = null;
				System.out.println("[DEBUG] destMMSI 파싱 시작 - MMSI: " + mmsi + ", getDestMMSI(): " + ship.getDestMMSI());
				if (ship.getDestMMSI() != null && !ship.getDestMMSI().isEmpty()) {
					System.out.println("[DEBUG] destMMSI 리스트 발견 - MMSI: " + mmsi + ", 크기: " + ship.getDestMMSI().size());
					destMMSIList = new java.util.ArrayList<>();
					for (String destMMSIStr : ship.getDestMMSI()) {
						try {
							long destMMSI = Long.parseLong(destMMSIStr);
							destMMSIList.add(destMMSI);
							System.out.println("[DEBUG] destMMSI 추가됨 - MMSI: " + mmsi + ", destMMSI: " + destMMSI);
						} catch (NumberFormatException e) {
							System.out.println("[DEBUG] ⚠️ 유효하지 않은 destMMSI 값 무시: " + destMMSIStr + " (MMSI: " + mmsi + ")");
						}
					}
					if (destMMSIList.isEmpty()) {
						System.out.println("[DEBUG] ⚠️ destMMSI 리스트가 비어있음 - MMSI: " + mmsi);
						destMMSIList = null; // 빈 리스트는 null로 처리하여 기존 동작 사용
					} else {
						System.out.println("[DEBUG] ✅ destMMSI 리스트 파싱 완료 - MMSI: " + mmsi + ", 크기: " + destMMSIList.size());
					}
				} else {
					System.out.println("[DEBUG] ⚠️ destMMSI 필드가 null이거나 비어있음 - MMSI: " + mmsi);
				}
				
				boolean result = globalEntityManager.controlMmsiState(mmsi, state, destMMSIList);
				if (result) {
					successCount++;
				} else {
					failCount++;
				}
			} catch (NumberFormatException e) {
				System.out.println("[DEBUG] ❌ 유효하지 않은 MMSI: " + ship.getMmsi());
				failCount++;
			} catch (Exception e) {
				System.out.println("[DEBUG] ❌ MMSI 상태 변경 실패: " + ship.getMmsi() + ", 오류: " + e.getMessage());
				failCount++;
			}
		}
		
		System.out.println("[DEBUG] ========== AIS 상태 제어 완료 ==========");
		System.out.println("[DEBUG] 성공: " + successCount + ", 실패: " + failCount);
	}
	
	/**
	 * ASM 상태 제어 메시지를 처리합니다.
	 * 토픽: mt/mg/traffic-ships/asm-state/{timestamp}
	 * 형식: [{"mmsi": "440301234", "state": "1", "size": "3", "asmPeriod": "0"}, ...]
	 * asmPeriod: "0"=단발 메시지, "4"~"360"=초 단위 주기
	 */
	private void processAsmStateMessage(String message) {
		System.out.println("[DEBUG] ========== ASM 상태 제어 메시지 처리 ==========");
		
		try {
			String trimmedMessage = message.trim();
			AsmControlMessage asmControlMessage;
			
			if (trimmedMessage.startsWith("[")) {
				// 배열 형식: [{"mmsi": "...", "state": "...", "size": "...", "asmPeriod": "..."}, ...]
				System.out.println("[DEBUG] 배열 형식 ASM 상태 제어 메시지 감지");
				java.lang.reflect.Type listTypeAsm = new TypeToken<List<AsmShipControl>>(){}.getType();
				List<AsmShipControl> ships = gson.fromJson(trimmedMessage, listTypeAsm);
				
				// AsmControlMessage 객체로 변환
				asmControlMessage = new AsmControlMessage();
				asmControlMessage.setShips(ships);
			} else {
				// 객체 형식: {"ships": [...]}
				asmControlMessage = gson.fromJson(trimmedMessage, AsmControlMessage.class);
			}
			
			if (asmControlMessage != null && asmControlMessage.getShips() != null) {
				handleAsmControlMessage(asmControlMessage);
			} else {
				System.out.println("[DEBUG] ⚠️ ASM 상태 제어 메시지 형식이 올바르지 않습니다.");
			}
		} catch (JsonSyntaxException e) {
			System.out.println("[DEBUG] ❌ ASM 상태 제어 메시지 JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * ASM 제어 메시지를 처리하여 MMSI의 ASM 메시지 생성 상태를 변경합니다.
	 * state에 따라 destMMSI 리스트를 관리하고, 리스트 상태에 따라 메시지 송신을 제어합니다.
	 */
	private void handleAsmControlMessage(com.all4land.generator.system.netty.dto.AsmControlMessage asmControlMessage) {
		if (asmControlMessage.getShips() == null || asmControlMessage.getShips().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ ASM 제어 메시지에 ships 데이터가 없습니다.");
			return;
		}
		
		int successCount = 0;
		int failCount = 0;
		
		// state="0"인 MMSI들을 먼저 수집 (중복 제거)
		java.util.Set<Long> mmsiToRemove = new java.util.HashSet<>();
		for (com.all4land.generator.system.netty.dto.AsmControlMessage.AsmShipControl ship : asmControlMessage.getShips()) {
			try {
				if ("0".equals(ship.getState())) {
					long mmsi = Long.parseLong(ship.getMmsi());
					mmsiToRemove.add(mmsi);
				}
			} catch (NumberFormatException e) {
				// 무시하고 계속 진행
			}
		}
		
		// state="0"인 MMSI들의 모든 AsmEntity 제거
		for (Long mmsi : mmsiToRemove) {
			try {
				com.all4land.generator.entity.MmsiEntity mmsiEntity = globalEntityManager.findMmsiEntity(mmsi);
				if (mmsiEntity == null) {
					System.out.println("[DEBUG] ⚠️ MMSI를 찾을 수 없음 (제거): " + mmsi);
					continue;
				}
				
				// 모든 AsmEntity 제거
				java.util.List<com.all4land.generator.entity.AsmEntity> removedEntities = mmsiEntity.removeAllAsmEntities();
				
				// 제거된 모든 AsmEntity의 스케줄러 Job 삭제
				for (com.all4land.generator.entity.AsmEntity asmEntity : removedEntities) {
					if (asmEntity != null && asmEntity.getAsmStartTimeJob() != null) {
						try {
							org.quartz.JobKey jobKey = asmEntity.getAsmStartTimeJob().getKey();
							scheduler.deleteJob(jobKey);
							System.out.println("[DEBUG] ✅ ASM 스케줄 제거 완료 - MMSI: " + mmsi + ", ServiceId: " + 
									(asmEntity.getServiceId() != null ? asmEntity.getServiceId() : "null"));
						} catch (Exception e) {
							System.out.println("[DEBUG] ⚠️ MMSI: " + mmsi + " ASM 스케줄 삭제 중 오류: " + e.getMessage());
						}
					}
				}
				
				System.out.println("[DEBUG] ✅ ASM 서비스 전체 제거 완료 - MMSI: " + mmsi + ", 제거된 개수: " + removedEntities.size());
				successCount++;
			} catch (Exception e) {
				System.out.println("[DEBUG] ❌ MMSI ASM 전체 제거 실패: " + mmsi + ", 오류: " + e.getMessage());
				e.printStackTrace();
				failCount++;
			}
		}
		
		// 각 메시지마다 인덱스 추적 (MMSI별, state="1"만 처리)
		java.util.Map<Long, Integer> mmsiIndexMap = new java.util.concurrent.ConcurrentHashMap<>();
		
		// state="1"인 메시지들 처리
		for (com.all4land.generator.system.netty.dto.AsmControlMessage.AsmShipControl ship : asmControlMessage.getShips()) {
			try {
				long mmsi = Long.parseLong(ship.getMmsi());
				String state = ship.getState(); // "0"=OFF, "1"=ON
				
				// state="0"인 경우는 이미 처리했으므로 건너뛰기
				if ("0".equals(state)) {
					continue;
				}
				
				// state="1"만 처리
				if ("1".equals(state)) {
					String size = ship.getSize();   // "1"~"3" (슬롯 점유 개수)
					String asmPeriod = ship.getAsmPeriod(); // "0"=단발, "4"~"360"=초 단위 주기
					
					// MMSI 엔티티 찾기
					com.all4land.generator.entity.MmsiEntity mmsiEntity = globalEntityManager.findMmsiEntity(mmsi);
					if (mmsiEntity == null) {
						System.out.println("[DEBUG] ❌ MMSI를 찾을 수 없음: " + mmsi);
						failCount++;
						continue;
					}
					
					// 인덱스 가져오기 및 증가
					int index = mmsiIndexMap.getOrDefault(mmsi, mmsiEntity.getAsmEntityCount());
					mmsiIndexMap.put(mmsi, index + 1);
					
					// ASM Entity 키 생성 (형식: "asm_" + mmsi + "_" + index)
					String asmEntityKey = "asm_" + mmsi + "_" + index;
					
					// 새로운 AsmEntity 생성
					com.all4land.generator.entity.AsmEntity asmEntity = new com.all4land.generator.entity.AsmEntity(
						eventPublisher,
						asmEntityKey
					);
					
					// size 설정
					if (size != null && !size.isEmpty()) {
						try {
							int slotCount = Integer.parseInt(size);
							if (slotCount >= 1 && slotCount <= 3) {
								asmEntity.setSlotCount(slotCount);
								System.out.println("[DEBUG] ✅ MMSI: " + mmsi + " ASM 슬롯 개수 설정: " + slotCount);
							}
						} catch (NumberFormatException e) {
							System.out.println("[DEBUG] ⚠️ MMSI: " + mmsi + " 슬롯 개수 파싱 실패: " + size);
						}
					}
					
					// asmPeriod 검증 및 설정
					String asmPeriodValue = validateAsmPeriod(asmPeriod);
					asmEntity.setAsmPeriod(asmPeriodValue);
					
					// destMMSI 리스트 처리
					if (ship.getDestMMSI() != null && !ship.getDestMMSI().isEmpty()) {
						for (String destMMSIStr : ship.getDestMMSI()) {
							try {
								long destMMSI = Long.parseLong(destMMSIStr);
								asmEntity.addDestMMSI(destMMSI, asmPeriodValue);
								System.out.println("[DEBUG] ✅ ASM destMMSI 추가 - MMSI: " + mmsi + 
										", ServiceId: " + asmEntityKey + ", destMMSI: " + destMMSI + ", asmPeriod: " + asmPeriodValue);
							} catch (NumberFormatException e) {
								System.out.println("[DEBUG] ⚠️ 유효하지 않은 ASM destMMSI 값 무시: " + destMMSIStr + " (MMSI: " + mmsi + ")");
							}
						}
					}
					
					// MmsiEntity에 AsmEntity 추가
					mmsiEntity.addAsmEntity(asmEntityKey, asmEntity);
					
					// destMMSI가 있으면 ASM 활성화 및 스케줄러 시작
					if (asmEntity.hasDestMMSI()) {
						// 첫 번째 시작 시간 설정
						asmEntity.setStartTime(java.time.LocalDateTime.now().plusSeconds(1), mmsiEntity);
						System.out.println("[DEBUG] ✅ 새로운 ASM 서비스 시작 - MMSI: " + mmsi + 
								", ServiceId: " + asmEntityKey + ", destMMSI 리스트 크기: " + asmEntity.getDestMMSIList().size());
					}
					
					successCount++;
				} else {
					System.out.println("[DEBUG] ⚠️ 유효하지 않은 state 값: " + state + " (MMSI: " + mmsi + ")");
					failCount++;
				}
			} catch (NumberFormatException e) {
				System.out.println("[DEBUG] ❌ 유효하지 않은 MMSI: " + ship.getMmsi());
				failCount++;
			} catch (Exception e) {
				System.out.println("[DEBUG] ❌ MMSI ASM 상태 변경 실패: " + ship.getMmsi() + ", 오류: " + e.getMessage());
				e.printStackTrace();
				failCount++;
			}
		}
		
		System.out.println("[DEBUG] ========== ASM 상태 제어 완료 ==========");
		System.out.println("[DEBUG] 성공: " + successCount + ", 실패: " + failCount);
	}
	
	/**
	 * asmPeriod 값 검증 및 정규화
	 * @param asmPeriod 검증할 asmPeriod 값
	 * @return 검증된 asmPeriod 값 ("0" 또는 "4"~"360"), 유효하지 않으면 "0"
	 */
	private String validateAsmPeriod(String asmPeriod) {
		if (asmPeriod == null || asmPeriod.isEmpty()) {
			return "0"; // 기본값
		}
		
		// "0"은 단발 메시지로 허용
		if ("0".equals(asmPeriod)) {
			return "0";
		}
		
		// 숫자로 변환 시도
		try {
			int period = Integer.parseInt(asmPeriod);
			// 4~360 범위 검증
			if (period >= 4 && period <= 360) {
				return String.valueOf(period);
			} else {
				System.out.println("[DEBUG] ⚠️ ASM Period 범위 초과: " + period + " (4~360 범위여야 함), 기본값 0으로 설정");
				return "0";
			}
		} catch (NumberFormatException e) {
			// 숫자가 아닌 경우 (예: 기존 "1" 값)
			System.out.println("[DEBUG] ⚠️ ASM Period 형식 오류: " + asmPeriod + " (숫자여야 함, 0 또는 4~360), 기본값 0으로 설정");
			return "0";
		}
	}
	
	/**
	 * 시뮬레이터 배속 상태 제어 메시지를 처리합니다.
	 * 토픽: mt/mg/simulator/sim-state/{timestamp}
	 * 형식: [{"state": "3", "simulationSpeed":"8"}]
	 */
	private void processSimStateMessage(String message) {
		System.out.println("[DEBUG] ========== 시뮬레이터 배속 상태 제어 메시지 처리 ==========");
		
		try {
			String trimmedMessage = message.trim();
			
			if (virtualTimeManager == null) {
				System.out.println("[DEBUG] ❌ VirtualTimeManager를 찾을 수 없습니다.");
				return;
			}
			
			// JSON 파싱 - 배열 형식 지원
			List<SimStateMessage> simStates;
			if (trimmedMessage.startsWith("[")) {
				// 배열 형식: [{"state": "3", "simulationSpeed":"8"}]
				System.out.println("[DEBUG] 배열 형식 시뮬레이터 배속 상태 제어 메시지 감지");
				java.lang.reflect.Type listType = new TypeToken<List<SimStateMessage>>(){}.getType();
				simStates = gson.fromJson(trimmedMessage, listType);
			} else if (trimmedMessage.startsWith("{")) {
				// 단일 객체 형식: {"state": "3", "simulationSpeed":"8"}
				SimStateMessage simState = gson.fromJson(trimmedMessage, SimStateMessage.class);
				simStates = java.util.Collections.singletonList(simState);
			} else {
				System.out.println("[DEBUG] ⚠️ 유효하지 않은 JSON 형식: " + message);
				return;
			}
			
			if (simStates == null || simStates.isEmpty()) {
				System.out.println("[DEBUG] ⚠️ 시뮬레이터 배속 상태 메시지에 데이터가 없습니다.");
				return;
			}
			
			// 첫 번째 메시지만 처리
			SimStateMessage simState = simStates.get(0);
			String simulationSpeed = simState.getSimulationSpeed();
			
			if (simulationSpeed == null || simulationSpeed.trim().isEmpty()) {
				System.out.println("[DEBUG] ⚠️ simulationSpeed 값이 없습니다.");
				return;
			}
			
			boolean success = virtualTimeManager.setSpeedMultiplier(simulationSpeed);
			if (success) {
				System.out.println("[DEBUG] ✅ 시뮬레이터 배속 변경 성공: " + simulationSpeed + "배");
				virtualTimeManager.printCurrentStatus();
			} else {
				System.out.println("[DEBUG] ❌ 시뮬레이터 배속 변경 실패: " + simulationSpeed + 
						"배 (1, 2, 4, 8배만 허용)");
			}
		} catch (JsonSyntaxException e) {
			System.out.println("[DEBUG] ❌ 시뮬레이터 배속 상태 메시지 JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ 시뮬레이터 배속 상태 메시지 처리 중 오류: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 시뮬레이터 배속 상태 메시지 DTO
	 */
	private static class SimStateMessage {
		private String state;
		private String simulationSpeed;
		
		public String getState() {
			return state;
		}
		
		public void setState(String state) {
			this.state = state;
		}
		
		public String getSimulationSpeed() {
			return simulationSpeed;
		}
		
		public void setSimulationSpeed(String simulationSpeed) {
			this.simulationSpeed = simulationSpeed;
		}
	}
	
	/**
	 * NMEA 스타일 제어 메시지를 처리합니다.
	 */
	private void processNmeaControlMessage(String nmeaMessage) {
		// SimpleTcpServerHandler의 로직을 재사용
		// 여기서는 간단히 처리하거나 별도로 구현
		System.out.println("[DEBUG] NMEA 제어 메시지 처리: " + nmeaMessage);
		// TODO: NMEA 메시지 처리 로직 추가
	}
	
	@Override
	public void onConnectionLost(Throwable cause) {
		System.out.println("[DEBUG] ⚠️ MQTT 연결 끊김: " + (cause != null ? cause.getMessage() : "Unknown"));
		if (cause != null) {
			cause.printStackTrace();
		}
	}
	
	/**
	 * TSQ 메시지를 처리합니다.
	 * 토픽: mg/ms/tsq/{mmsi}/{yyyyMMddhhmmss.SSSS}/
	 * 
	 * @param message JSON 메시지
	 * @param sourceMmsi 토픽에서 추출한 MMSI
	 */
	private void processTsqMessage(String message, String sourceMmsi) {
		System.out.println("[DEBUG] ========== TSQ 메시지 처리 시작 ==========");
		System.out.println("[DEBUG] Source MMSI: " + sourceMmsi);
		
		try {
			String trimmedMessage = message.trim();
			List<TsqResourceRequestMessage> tsqRequests;
			
			if (trimmedMessage.startsWith("[")) {
				// 배열 형식
				java.lang.reflect.Type listType = new TypeToken<List<TsqResourceRequestMessage>>(){}.getType();
				tsqRequests = gson.fromJson(trimmedMessage, listType);
			} else {
				// 단일 객체 형식
				TsqResourceRequestMessage singleRequest = gson.fromJson(trimmedMessage, TsqResourceRequestMessage.class);
				tsqRequests = java.util.Arrays.asList(singleRequest);
			}
			
			if (tsqRequests != null && !tsqRequests.isEmpty()) {
				handleTsqResourceRequest(tsqRequests, sourceMmsi);
			} else {
				System.out.println("[DEBUG] ⚠️ TSQ 메시지가 비어있습니다.");
			}
		} catch (JsonSyntaxException e) {
			System.out.println("[DEBUG] ❌ TSQ 메시지 JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ TSQ 메시지 처리 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * TSQ 리소스 요청 메시지를 처리합니다.
	 * 각 요청에 대해 TSQ 메시지를 생성하고 전송합니다.
	 * 
	 * 메시지 형식:
	 * $VATSQ,111,seq,sourceMmsi,sourceMmsi,destMmsi,destMmsi,physicalChannelNumber,linkId*CRC
	 * \r\n ESI 메시지
	 * \r\n 용량,serviceType
	 * 
	 * @param tsqRequests TSQ 요청 리스트
	 * @param sourceMmsi 송신 MMSI (토픽에서 추출)
	 */
	private void handleTsqResourceRequest(List<TsqResourceRequestMessage> tsqRequests, String sourceMmsi) {
		System.out.println("[DEBUG] ========== TSQ 리소스 요청 처리 시작 ==========");
		System.out.println("[DEBUG] 요청 개수: " + tsqRequests.size());
		
		if (tsqRequests == null || tsqRequests.isEmpty()) {
			System.out.println("[DEBUG] ⚠️ TSQ 리소스 요청이 비어있습니다.");
			return;
		}
		
		// 현재 시간 기반 slotNumber 계산
		LocalDateTime now = LocalDateTime.now();
		String formatNow = now.format(SystemConstMessage.formatterForStartIndex);
		int slotNumber = timeMapRangeCompnents.findSlotNumber(formatNow);
		
		if (slotNumber == -1) {
			System.out.println("[DEBUG] ❌ 현재 시간에 대한 슬롯 번호를 찾을 수 없습니다: " + formatNow);
			return;
		}
		
		// TDMA 프레임 계산 (slotNumber / 90)
		int tdmaFrame = slotNumber / 90;
		
		System.out.println("[DEBUG] 현재 시간: " + formatNow + ", 슬롯 번호: " + slotNumber + ", TDMA 프레임: " + tdmaFrame);
		
		// GlobalEntityManager에서 UUID 값 가져오기
		String shoreStationId = globalEntityManager.getUuid();
		
		// 각 TSQ 요청 처리
		for (TsqResourceRequestMessage request : tsqRequests) {
			try {
				String service = request.getService();
				String serviceSize = request.getServiceSize();
				List<String> destMMSIList = request.getDestMMSI();
				String nmeaMessage = request.getNmea();
				
				if (destMMSIList == null || destMMSIList.isEmpty()) {
					System.out.println("[DEBUG] ⚠️ destMMSI 리스트가 비어있습니다. Service: " + service);
					continue;
				}
				
				if (nmeaMessage == null || nmeaMessage.trim().isEmpty()) {
					System.out.println("[DEBUG] ⚠️ NMEA 메시지가 비어있습니다. Service: " + service);
					continue;
				}
				
				System.out.println("[DEBUG] TSQ 요청 처리 - Service: " + service + ", ServiceSize: " + serviceSize + 
						", SourceMMSI: " + sourceMmsi + ", DestMMSI 개수: " + destMMSIList.size());
				
				// 각 destMMSI에 대해 메시지 전송
				for (String destMmsi : destMMSIList) {
					try {
						// 시퀀스 번호 증가
						int seq = tsqSeq.getAndIncrement();
						if (seq >= 1000) {
							tsqSeq.set(1);
							seq = 1;
						}
						
						// ESI 메시지 생성
						String linkId = "19";
						String channelLeg = "0";
						String tdmachannel = "0";
						String totalAccountSlot = "14";
						String firstSlotNumber = String.valueOf(slotNumber);
						
						ESIMessageUtil esi = new ESIMessageUtil(shoreStationId, channelLeg, String.valueOf(tdmaFrame), 
								tdmachannel, totalAccountSlot, firstSlotNumber, linkId);
						String esiMessage = esi.getMessage();
						
						// 전체 메시지 조립
						// 형식: NMEA (TSQ) + CRLF + ESI 메시지 + CRLF + 용량,serviceType
						StringBuilder sb = new StringBuilder();
						sb.append(nmeaMessage).append(SystemConstMessage.CRLF);
						sb.append(esiMessage).append(SystemConstMessage.CRLF);
						sb.append(serviceSize).append(",").append(service).append(SystemConstMessage.CRLF);
						
						String fullMessage = sb.toString();
						System.out.println("[DEBUG] 생성된 TSQ 메시지 (DestMMSI: " + destMmsi + "):\n" + fullMessage);
						
						// TCP 클라이언트로 전송
						if (tcpServerTableModel != null) {
							for (TcpServerTableEntity tcpServerTableEntity : tcpServerTableModel.getTcpServerTableEntitys()) {
								if (tcpServerTableEntity.isStatus() && tcpServerTableEntity.getNettyServerTCPConfiguration() != null) {
									for (TcpTargetClientInfoEntity tcpTargetClientInfoEntity : tcpServerTableEntity.getTcpTargetClientInfoEntitys()) {
										if (tcpTargetClientInfoEntity.isTsq()) {
											Channel clientChannel = tcpTargetClientInfoEntity.getClientChannel();
											String ip = tcpTargetClientInfoEntity.getIp();
											int port = tcpTargetClientInfoEntity.getPort();
											
											NettyServerTCPConfiguration nettyServerTCPConfiguration = 
													tcpServerTableEntity.getNettyServerTCPConfiguration();
											nettyServerTCPConfiguration.sendToClient(clientChannel, ip, port, fullMessage);
											
											System.out.println("[DEBUG] ✅ TSQ 메시지 TCP 전송 완료 - DestMMSI: " + destMmsi + 
													", IP: " + ip + ", Port: " + port);
										}
									}
								}
							}
						}
						
						// UDP 클라이언트로 전송
						if (udpServerTableModel != null) {
							for (UdpServerTableEntity udpServerTableEntity : udpServerTableModel.getUdpServerTableEntitys()) {
								if (udpServerTableEntity.isStatus() && udpServerTableEntity.getNettyServerUDPConfiguration() != null) {
									NettyServerUDPConfiguration nettyServerUDPConfiguration = 
											udpServerTableEntity.getNettyServerUDPConfiguration();
									nettyServerUDPConfiguration.sendToClient(fullMessage);
									
									System.out.println("[DEBUG] ✅ TSQ 메시지 UDP 전송 완료 - DestMMSI: " + destMmsi);
								}
							}
						}
						
					} catch (Exception e) {
						System.out.println("[DEBUG] ❌ DestMMSI " + destMmsi + "에 대한 TSQ 메시지 전송 중 오류: " + e.getMessage());
						e.printStackTrace();
					}
				}
				
			} catch (Exception e) {
				System.out.println("[DEBUG] ❌ TSQ 리소스 요청 처리 중 오류 발생: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		System.out.println("[DEBUG] ========== TSQ 리소스 요청 처리 완료 ==========");
	}
}

