package com.all4land.generator.system.mqtt;

import org.quartz.Scheduler;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.SlotStateManager;
import com.all4land.generator.system.component.SimulationStateManager;
import com.all4land.generator.system.component.TimeMapRangeCompnents;
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.netty.dto.CreateMmsiRequest;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.system.schedule.job.TsqEntityChangeStartDateQuartz;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.all4land.generator.system.netty.dto.AsmControlMessage.AsmShipControl;
import com.all4land.generator.system.netty.dto.AsmControlMessage;
import com.all4land.generator.system.netty.dto.TsqResourceRequestMessage;
import com.all4land.generator.system.netty.dto.TsqMqttResponseMessage;
import com.all4land.generator.system.queue.TsqMessageQueue;
import com.all4land.generator.ui.tab.ais.model.TcpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpServerTableModel;
import com.all4land.generator.ais.ESIMessageUtil;
import com.all4land.generator.ais.TerrestrialSlotResourceRequest;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.ui.tab.ais.entity.TcpServerTableEntity;
import com.all4land.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;
import com.all4land.generator.ui.tab.ais.entity.UdpServerTableEntity;
import com.all4land.generator.system.netty.send.config.NettyServerTCPConfiguration;
import com.all4land.generator.system.netty.send.config.NettyServerUDPConfiguration;
import io.netty.channel.Channel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * MQTT 메시지를 처리하는 클래스
 * TCP 서버와 동일한 방식으로 메시지를 처리합니다.
 */
@Slf4j
public class MqttMessageProcessor implements MqttMessageCallback {
	
	private final GlobalEntityManager globalEntityManager;
	private final Scheduler scheduler;
	private final QuartzCoreService quartzCoreService;
	private final VirtualTimeManager virtualTimeManager;
	private final ApplicationEventPublisher eventPublisher;
	private final TcpServerTableModel tcpServerTableModel;
	private final UdpServerTableModel udpServerTableModel;
	private final TimeMapRangeCompnents timeMapRangeCompnents;
	private final SlotStateManager slotStateManager;
	private final TsqMessageQueue tsqMessageQueue;
	private final MqttClientConfiguration mqttClient;
	private final SimulationStateManager simulationStateManager;
	private final Gson gson = new Gson();
	private final AtomicInteger tsqSeq = new AtomicInteger(1);
	private final AtomicBoolean isProcessingQueue = new AtomicBoolean(false);
	
	public MqttMessageProcessor(GlobalEntityManager globalEntityManager,
			Scheduler scheduler,
			QuartzCoreService quartzCoreService,
			VirtualTimeManager virtualTimeManager,
			ApplicationEventPublisher eventPublisher,
			TcpServerTableModel tcpServerTableModel,
			UdpServerTableModel udpServerTableModel,
			TimeMapRangeCompnents timeMapRangeCompnents,
			SlotStateManager slotStateManager,
			TsqMessageQueue tsqMessageQueue,
			MqttClientConfiguration mqttClient,
			SimulationStateManager simulationStateManager) {
		this.globalEntityManager = globalEntityManager;
		this.scheduler = scheduler;
		this.quartzCoreService = quartzCoreService;
		this.virtualTimeManager = virtualTimeManager;
		this.eventPublisher = eventPublisher;
		this.tcpServerTableModel = tcpServerTableModel;
		this.udpServerTableModel = udpServerTableModel;
		this.timeMapRangeCompnents = timeMapRangeCompnents;
		this.slotStateManager = slotStateManager;
		this.tsqMessageQueue = tsqMessageQueue;
		this.mqttClient = mqttClient;
		this.simulationStateManager = simulationStateManager;
	}
	
	@Override
	public void onMessage(String topic, String message) {
		
		// 토픽에서 정보 추출
		// 형식: {송신 식별자}/{수신 식별자}/{카테고리}/{액션}/{timestamp}
		//   예시: mt/mg/traffic-ships/create/20260116112354
		String normalizedTopic = topic.startsWith("/") ? topic.substring(1) : topic;
		String[] topicParts = normalizedTopic.split("/");
		
		String senderId = null;
		String receiverId = null;
		String category = null;
		String action = null;
		String timestamp = null;
		
		if (topicParts.length >= 5) {
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
					case "tsq-state":
						// TSQ 상태 제어: traffic-ships/tsq-state/{timestamp}
						processTsqStateMessage(message);
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
				handleArrayFormat(trimmedMessage);
			} else if (trimmedMessage.startsWith("{")) {
				// 객체 형식: CreateMmsiRequest로 파싱
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
				
				// testMmsi 리스트 추출 및 변환
				List<Long> testMMSIList = null;
				if (ship.getTestMmsi() != null && !ship.getTestMmsi().isEmpty()) {
					testMMSIList = new java.util.ArrayList<>();
					for (String testMMSIStr : ship.getTestMmsi()) {
						try {
							long testMMSI = Long.parseLong(testMMSIStr);
							testMMSIList.add(testMMSI);
						} catch (NumberFormatException e) {
							System.out.println("[DEBUG] ⚠️ 유효하지 않은 testMmsi 값 무시: " + testMMSIStr + " (MMSI: " + mmsi + ")");
						}
					}
					if (testMMSIList.isEmpty()) {
						System.out.println("[DEBUG] ⚠️ testMmsi 리스트가 비어있음 - MMSI: " + mmsi);
						testMMSIList = null; // 빈 리스트는 null로 처리하여 기존 동작 사용
					} else {
						System.out.println("[DEBUG] ✅ testMmsi 리스트 파싱 완료 - MMSI: " + mmsi +", state: "+ state + ", 피시험 선박: " + testMMSIList);
					}
				} else {
					System.out.println("[DEBUG] ⚠️ testMmsi 필드가 null이거나 비어있음 - MMSI: " + mmsi);
				}
				
				boolean result = globalEntityManager.controlMmsiState(mmsi, state, testMMSIList);
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
	 * 형식: [{"serviceId":"s2234567","sourceMmsi":"440301234","testMmsi":["440123456"],"state":"1","size":"3","asmPeriod":"30"}, ...]
	 * asmPeriod: "0"=단발 메시지, "4"~"360"=초 단위 주기
	 */
	private void processAsmStateMessage(String message) {
		System.out.println("[DEBUG] ========== ASM 상태 제어 메시지 처리 ==========");
		
		try {
			String trimmedMessage = message.trim();
			AsmControlMessage asmControlMessage;
			
			if (trimmedMessage.startsWith("[")) {
				// 배열 형식: [{"serviceId":"...", "sourceMmsi":"...", "testMmsi":[...], "state": "...", "size": "...", "asmPeriod": "..."}, ...]
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
	 * state에 따라 testMmsi 리스트를 관리하고, 리스트 상태에 따라 메시지 송신을 제어합니다.
	 */
	private void handleAsmControlMessage(com.all4land.generator.system.netty.dto.AsmControlMessage asmControlMessage) {
		if (asmControlMessage.getShips() == null || asmControlMessage.getShips().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ ASM 제어 메시지에 ships 데이터가 없습니다.");
			return;
		}
		
		int successCount = 0;
		int failCount = 0;
		
		// 각 항목별로 처리 (state="0" 또는 state="1")
		for (com.all4land.generator.system.netty.dto.AsmControlMessage.AsmShipControl ship : asmControlMessage.getShips()) {
			try {
				String serviceId = ship.getServiceId();
				String sourceMmsiStr = ship.getSourceMmsi();
				String state = ship.getState(); // "0"=OFF, "1"=ON
				
				// 필수 필드 검증
				if (serviceId == null || serviceId.trim().isEmpty()) {
					System.out.println("[DEBUG] ⚠️ serviceId가 비어있습니다.");
					failCount++;
					continue;
				}
				if (sourceMmsiStr == null || sourceMmsiStr.trim().isEmpty()) {
					System.out.println("[DEBUG] ⚠️ sourceMmsi가 비어있습니다.");
					failCount++;
					continue;
				}
				
				long sourceMmsi;
				try {
					sourceMmsi = Long.parseLong(sourceMmsiStr);
				} catch (NumberFormatException e) {
					System.out.println("[DEBUG] ❌ 유효하지 않은 sourceMmsi: " + sourceMmsiStr);
					failCount++;
					continue;
				}
				
				if ("0".equals(state)) {
					// state="0": 특정 serviceId의 AsmEntity만 제거
					com.all4land.generator.entity.MmsiEntity mmsiEntity = globalEntityManager.findMmsiEntity(sourceMmsi);
					if (mmsiEntity == null) {
						System.out.println("[DEBUG] ⚠️ sourceMmsi를 찾을 수 없음 (제거): " + sourceMmsi);
						failCount++;
						continue;
					}
					
					// serviceId로 특정 AsmEntity만 제거
					com.all4land.generator.entity.AsmEntity removedEntity = mmsiEntity.removeAsmEntity(serviceId);
					
					if (removedEntity != null) {
						// 스케줄러 Job 삭제
						if (removedEntity.getAsmStartTimeJob() != null) {
							try {
								org.quartz.JobKey jobKey = removedEntity.getAsmStartTimeJob().getKey();
								scheduler.deleteJob(jobKey);
								System.out.println("[DEBUG] ✅ ASM 스케줄 제거 완료 - SourceMmsi: " + sourceMmsi + 
										", ServiceId: " + serviceId);
							} catch (Exception e) {
								System.out.println("[DEBUG] ⚠️ SourceMmsi: " + sourceMmsi + ", ServiceId: " + serviceId + 
										" ASM 스케줄 삭제 중 오류: " + e.getMessage());
							}
						}
						System.out.println("[DEBUG] ✅ ASM 서비스 제거 완료 - SourceMmsi: " + sourceMmsi + 
								", ServiceId: " + serviceId);
						successCount++;
					} else {
						System.out.println("[DEBUG] ⚠️ ASM 서비스를 찾을 수 없음 - SourceMmsi: " + sourceMmsi + 
								", ServiceId: " + serviceId);
						failCount++;
					}
					
				} else if ("1".equals(state)) {
					// state="1": ASM 서비스 시작/추가
					String size = ship.getSize();   // "1"~"3" (슬롯 점유 개수)
					String asmPeriod = ship.getAsmPeriod(); // "0"=단발, "4"~"360"=초 단위 주기
					
					// sourceMmsi로 MmsiEntity 조회, 없으면 자동 생성
					com.all4land.generator.entity.MmsiEntity mmsiEntity = globalEntityManager.findMmsiEntity(sourceMmsi);
					if (mmsiEntity == null) {
						System.out.println("[DEBUG] ⚠️ SourceMmsi를 찾을 수 없음, 자동 생성 시도: " + sourceMmsi);
						try {
							mmsiEntity = globalEntityManager.ensureMmsiEntity(sourceMmsi, scheduler, quartzCoreService);
							System.out.println("[DEBUG] ✅ SourceMmsi 자동 생성 완료: " + sourceMmsi);
						} catch (Exception e) {
							System.out.println("[DEBUG] ❌ SourceMmsi 자동 생성 실패: " + sourceMmsi + ", 오류: " + e.getMessage());
							failCount++;
							continue;
						}
					}
					
					// 새로운 AsmEntity 생성 (serviceId를 키로 사용)
					com.all4land.generator.entity.AsmEntity asmEntity = new com.all4land.generator.entity.AsmEntity(
						eventPublisher,
						serviceId
					);
					
					// size 설정
					if (size != null && !size.isEmpty()) {
						try {
							int slotCount = Integer.parseInt(size);
							if (slotCount >= 1 && slotCount <= 3) {
								asmEntity.setSlotCount(slotCount);
								System.out.println("[DEBUG] ✅ SourceMmsi: " + sourceMmsi + " ASM 슬롯 개수 설정: " + slotCount);
							}
						} catch (NumberFormatException e) {
							System.out.println("[DEBUG] ⚠️ SourceMmsi: " + sourceMmsi + " 슬롯 개수 파싱 실패: " + size);
						}
					}
					
					// asmPeriod 검증 및 설정
					String asmPeriodValue = validateAsmPeriod(asmPeriod);
					asmEntity.setAsmPeriod(asmPeriodValue);
					
					// testMmsi 리스트 처리
					if (ship.getTestMmsi() != null && !ship.getTestMmsi().isEmpty()) {
						for (String testMmsiStr : ship.getTestMmsi()) {
							try {
								long testMmsi = Long.parseLong(testMmsiStr);
								asmEntity.addTestMMSI(testMmsi, asmPeriodValue);
								System.out.println("[DEBUG] ✅ ASM testMmsi 추가 - SourceMmsi: " + sourceMmsi + 
										", ServiceId: " + serviceId + ", testMmsi: " + testMmsi + ", asmPeriod: " + asmPeriodValue);
							} catch (NumberFormatException e) {
								System.out.println("[DEBUG] ⚠️ 유효하지 않은 ASM testMmsi 값 무시: " + testMmsiStr + 
										" (SourceMmsi: " + sourceMmsi + ")");
							}
						}
					}
					
					// MmsiEntity에 AsmEntity 추가 (serviceId를 키로 사용)
					mmsiEntity.addAsmEntity(serviceId, asmEntity);
					
					// testMmsi가 있으면 ASM 활성화 및 스케줄러 시작
					if (asmEntity.hasTestMMSI()) {
						// 첫 번째 시작 시간 설정
						asmEntity.setStartTime(java.time.LocalDateTime.now().plusSeconds(1), mmsiEntity);
						System.out.println("[DEBUG] ✅ 새로운 ASM 서비스 시작 - SourceMmsi: " + sourceMmsi + 
								", ServiceId: " + serviceId + ", testMmsi 리스트 크기: " + asmEntity.getTestMMSIList().size());
					}
					
					successCount++;
				} else {
					System.out.println("[DEBUG] ⚠️ 유효하지 않은 state 값: " + state + " (SourceMmsi: " + sourceMmsi + 
							", ServiceId: " + serviceId + ")");
					failCount++;
				}
			} catch (Exception e) {
				System.out.println("[DEBUG] ❌ ASM 상태 변경 실패, 오류: " + e.getMessage());
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
	 * 시뮬레이터 배속 및 상태 제어 메시지를 처리합니다.
	 * 토픽: mt/mg/simulator/sim-state/{timestamp}
	 * 형식: [{"state": "1", "simulationSpeed":"8"}]
	 * state: 1=RUN, 2=PAUSE, 3=배속변경, 4=STOP
	 */
	private void processSimStateMessage(String message) {
		System.out.println("[DEBUG] ========== 시뮬레이터 상태 제어 메시지 처리 ==========");
		
		try {
			String trimmedMessage = message.trim();
			
			if (simulationStateManager == null) {
				System.out.println("[DEBUG] ❌ SimulationStateManager를 찾을 수 없습니다.");
				return;
			}
			
			// JSON 파싱 - 배열 형식 지원
			List<SimStateMessage> simStates;
			if (trimmedMessage.startsWith("[")) {
				// 배열 형식: [{"state": "3", "simulationSpeed":"8"}]
				System.out.println("[DEBUG] 배열 형식 시뮬레이터 상태 제어 메시지 감지");
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
				System.out.println("[DEBUG] ⚠️ 시뮬레이터 상태 메시지에 데이터가 없습니다.");
				return;
			}
			
			// 첫 번째 메시지만 처리
			SimStateMessage simState = simStates.get(0);
			String state = simState.getState();
			String simulationSpeed = simState.getSimulationSpeed();
			
			// state 값에 따라 처리
			switch (state) {
				case "1": // RUN (재개)
					simulationStateManager.run();
					System.out.println("[DEBUG] ✅ 시뮬레이션 재개/시작");
					break;
					
				case "2": // PAUSE
					simulationStateManager.pause();
					System.out.println("[DEBUG] ⏸️ 시뮬레이션 일시정지");
					break;
					
				case "3": // 배속 변경
					if (simulationSpeed != null && !simulationSpeed.trim().isEmpty()) {
						boolean success = virtualTimeManager.setSpeedMultiplier(simulationSpeed);
						if (success) {
							System.out.println("[DEBUG] ✅ 배속 변경: " + simulationSpeed + "배");
							virtualTimeManager.printCurrentStatus();
						} else {
							System.out.println("[DEBUG] ❌ 배속 변경 실패: " + simulationSpeed + 
									"배 (1, 2, 4, 8배만 허용)");
						}
					} else {
						System.out.println("[DEBUG] ⚠️ simulationSpeed 값이 없습니다.");
					}
					break;
					
				case "4": // STOP
					simulationStateManager.stop();
					System.out.println("[DEBUG] 🛑 시뮬레이션 완전 중단");
					break;
					
				default:
					System.out.println("[DEBUG] ⚠️ 알 수 없는 state: " + state);
			}
		} catch (JsonSyntaxException e) {
			System.out.println("[DEBUG] ❌ JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ 상태 메시지 처리 중 오류: " + e.getMessage());
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
	 * TSQ 상태 메시지를 처리하고 큐에 저장합니다.
	 * 토픽: mt/mg/traffic-ships/tsq-state/{timestamp}
	 * 
	 * @param message JSON 메시지 (TSQ 요청 리스트)
	 */
	private void processTsqStateMessage(String message) {
		System.out.println("[DEBUG] ========== TSQ 상태 메시지 처리 시작 ==========");
		
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
				// 각 TSQ 요청을 큐에 저장
				for (TsqResourceRequestMessage request : tsqRequests) {
					tsqMessageQueue.offer(request);
				}
				System.out.println("[DEBUG] ✅ TSQ 요청 " + tsqRequests.size() + "개를 큐에 저장 완료");
				
				// 큐에 메시지가 추가되었으므로 처리 시작
				processTsqQueue();
			} else {
				System.out.println("[DEBUG] ⚠️ TSQ 메시지가 비어있습니다.");
			}
		} catch (JsonSyntaxException e) {
			System.out.println("[DEBUG] ❌ TSQ 상태 메시지 JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ TSQ 상태 메시지 처리 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * TSQ 큐에 있는 메시지들을 처리합니다.
	 * TSQ_TEST_SLOT_NUMBER_ALL 세트에서 비어있는 슬롯을 찾아 메시지를 전송합니다.
	 * 동시성 제어를 위해 AtomicBoolean을 사용하여 한 번에 하나의 스레드만 실행됩니다.
	 */
	public void processTsqQueue() {
		// 이미 처리 중이면 스킵
		if (!isProcessingQueue.compareAndSet(false, true)) {
			System.out.println("[DEBUG] ⚠️ processTsqQueue()가 이미 실행 중입니다. 스킵합니다.");
			return;
		}
		
		try {
			// 큐가 비어있지 않으면 계속 처리
			while (!tsqMessageQueue.isEmpty()) {
				// 큐에서 하나의 요청 확인 (peek로 확인, 처리 성공 시에만 poll)
				TsqResourceRequestMessage request = tsqMessageQueue.peek();
				if (request == null) {
					break;
				}
				
				// TSQ_TEST_SLOT_NUMBER_ALL 세트에서 비어있는 슬롯 찾기
				int availableSlot = findAvailableTsqSlot();
				
				if (availableSlot == -1) {
					// 전송 가능한 슬롯이 없으면 대기 (다음에 다시 시도)
					System.out.println("[DEBUG] ⚠️ 전송 가능한 TSQ 슬롯이 없습니다. 큐에 " + tsqMessageQueue.size() + "개 메시지 대기 중...");
					break; // 나중에 다시 시도하도록 큐에 유지
				}
				
				// 슬롯을 찾았으면 큐에서 제거하고 전송
				tsqMessageQueue.poll();
				sendTsqMessage(request, availableSlot);
			}
		} finally {
			isProcessingQueue.set(false);
		}
	}
	
	/**
	 * TSQ_TEST_SLOT_NUMBER_ALL 세트에서 비어있는 슬롯을 찾습니다.
	 * 가상 시간의 현재 슬롯 번호 이후부터 검색합니다.
	 * @return 비어있는 슬롯 번호, 없으면 -1
	 */
	private int findAvailableTsqSlot() {
		// 가상 시간 기반 현재 슬롯 번호 계산
		LocalDateTime virtualTime = virtualTimeManager.getCurrentVirtualTime();
		String formatNow = virtualTime.format(SystemConstMessage.formatterForStartIndex);
		int currentSlotNumber = timeMapRangeCompnents.findSlotNumber(formatNow);

		if (currentSlotNumber == -1) {
			log.warn("현재 시간에 대한 슬롯 번호를 찾을 수 없습니다: {}", formatNow);
			// 슬롯 번호를 찾을 수 없으면 처음부터 검색
			currentSlotNumber = 0;
		}
		
		// TSQ_TEST_SLOT_NUMBER_ALL 세트를 정렬된 리스트로 변환
		Set<Integer> tsqSlots = SystemConstMessage.TSQ_TEST_SLOT_NUMBER_ALL;
		List<Integer> sortedSlots = new ArrayList<>(tsqSlots);
		
		Collections.sort(sortedSlots);
		
		// 현재 슬롯 번호 이후부터 검색
		int startIndex = -1;
		for (int i = 0; i < sortedSlots.size(); i++) {
			if (sortedSlots.get(i) > currentSlotNumber) {
				startIndex = i;
				break;
			}
		}
		
		// 현재 슬롯 이후부터 끝까지 검색
		if (startIndex != -1) {
			for (int i = startIndex; i < sortedSlots.size(); i++) {
				Integer slotNumber = sortedSlots.get(i);
				if (!slotStateManager.isSlotOccupied(slotNumber)) {
					System.out.println("[DEBUG] 전송 가능한 TSQ 슬롯 발견 (현재 슬롯 " + currentSlotNumber + " 이후): " + slotNumber);
					return slotNumber;
				}
			}
		}
		
		// 현재 슬롯 이후에 사용 가능한 슬롯이 없으면 처음부터 현재 슬롯 전까지 검색 (순환)
		int endIndex = (startIndex != -1) ? startIndex : sortedSlots.size();
		for (int i = 0; i < endIndex; i++) {
			Integer slotNumber = sortedSlots.get(i);
			if (!slotStateManager.isSlotOccupied(slotNumber)) {
				System.out.println("[DEBUG] 전송 가능한 TSQ 슬롯 발견 (순환, 현재 슬롯 " + currentSlotNumber + " 이전): " + slotNumber);
				return slotNumber;
			}
		}
		
		return -1; // 전송 가능한 슬롯 없음
	}
	
	/**
	 * 슬롯 번호를 가상 시간으로 변환합니다.
	 * @param slotNumber 슬롯 번호
	 * @return 해당 슬롯의 시작 시간 (가상 시간)
	 */
	private LocalDateTime convertSlotNumberToVirtualTime(int slotNumber) {
		com.all4land.generator.ui.tab.ais.entity.Range range = timeMapRangeCompnents.getRange(slotNumber);
		if (range == null) {
			System.out.println("[DEBUG] ⚠️ 슬롯 번호에 대한 Range를 찾을 수 없습니다: " + slotNumber);
			return virtualTimeManager.getCurrentVirtualTime();
		}
		
		// 현재 가상 시간의 분, 시, 일 등을 가져옴
		LocalDateTime currentVirtualTime = virtualTimeManager.getCurrentVirtualTime();
		
		// 슬롯의 시작 시간 (ss.SSSS 형식)을 초 단위로 변환
		double slotStartSecond = range.getFrom();
		int seconds = (int) slotStartSecond;
		int nanos = (int) ((slotStartSecond - seconds) * 1_000_000_000);
		
		// 현재 시간의 초 부분을 슬롯 시작 시간으로 교체
		LocalDateTime slotTime = currentVirtualTime
				.withSecond(seconds)
				.withNano(nanos);
		
		// 만약 슬롯 시간이 현재 시간보다 이전이면 다음 분으로 설정
		if (slotTime.isBefore(currentVirtualTime)) {
			slotTime = slotTime.plusMinutes(1);
		}
		
		return slotTime;
	}
	
	/**
	 * TSQ 메시지를 생성하고 MQTT로 전송합니다.
	 * @param request TSQ 리소스 요청 메시지
	 * @param slotNumber 전송할 슬롯 번호
	 */
	private void sendTsqMessage(TsqResourceRequestMessage request, int slotNumber) {
		System.out.println("[DEBUG] ========== TSQ 메시지 전송 시작 ==========");
		
		// 필수 필드 검증
		if (request.getServiceId() == null || request.getServiceId().trim().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ serviceId가 비어있습니다.");
			return;
		}
		if (request.getType() == null || request.getType().trim().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ type이 비어있습니다.");
			return;
		}
		if (request.getSize() == null || request.getSize().trim().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ size가 비어있습니다.");
			return;
		}
		if (request.getSourceMmsi() == null || request.getSourceMmsi().trim().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ sourceMmsi가 비어있습니다.");
			return;
		}
		
		if (request.getTestMmsi() == null || request.getTestMmsi().trim().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ testMmsi가 비어있습니다.");
			return;
		}
		
		// 슬롯 번호를 가상 시간으로 변환
		LocalDateTime slotVirtualTime = convertSlotNumberToVirtualTime(slotNumber);
		
		// 가상 시간을 실제 시간으로 변환 (Quartz는 실제 시간을 사용)
		LocalDateTime slotRealTime = virtualTimeManager.convertVirtualToRealTime(slotVirtualTime);
		
		// System.out.println("[DEBUG] TSQ 메시지 스케줄링 - Service: " + request.getServiceId() + 
		// 		", SlotNumber: " + slotNumber + 
		// 		", 가상 시간: " + slotVirtualTime + 
		// 		", 실제 시간: " + slotRealTime);
		
		// 슬롯 점유 처리 (즉시 점유)
		try {
			Long sourceMmsiLong = Long.parseLong(request.getSourceMmsi());
			boolean occupied = slotStateManager.occupySlot(slotNumber, sourceMmsiLong, 
					slotVirtualTime, null, "TSQ");
			if (!occupied) {
				System.out.println("[DEBUG] ⚠️ 슬롯 " + slotNumber + " 점유 실패 (이미 점유됨)");
				// 큐에 다시 넣기
				tsqMessageQueue.offer(request);
				return;
			}
			System.out.println("[DEBUG] ✅ 슬롯 " + slotNumber + " 점유 완료");
		} catch (NumberFormatException e) {
			System.out.println("[DEBUG] ❌ sourceMmsi 파싱 오류: " + request.getSourceMmsi());
			return;
		}
		
		// Quartz Job 스케줄링
		try {
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("tsqRequest", request);
			jobDataMap.put("slotNumber", slotNumber);
			
			String jobKey = "tsq_" + request.getServiceId() + "_" + slotNumber + "_" + slotVirtualTime.toString();
			
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(jobKey, "tsqGroup")
					.startAt(Date.from(slotRealTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
			
			JobDetail job = JobBuilder.newJob(TsqEntityChangeStartDateQuartz.class)
					.withIdentity(jobKey, "tsqGroup")
					.storeDurably(true)
					.setJobData(jobDataMap)
					.build();
			
			scheduler.scheduleJob(job, trigger);
			
			System.out.println("[DEBUG] ✅ TSQ 메시지 스케줄링 완료 - Service: " + request.getServiceId() + 
					", SlotNumber: " + slotNumber + 
					", 실행 시간: " + slotRealTime);
			
			// 전송 후 다음 큐 메시지 처리
			processTsqQueue();
			
		} catch (SchedulerException e) {
			System.out.println("[DEBUG] ❌ TSQ 메시지 스케줄링 중 오류: " + e.getMessage());
			e.printStackTrace();
			// 스케줄링 실패 시 슬롯 해제
			slotStateManager.releaseSlot(slotNumber);
			// 큐에 다시 넣기
			tsqMessageQueue.offer(request);
		}
		
		System.out.println("[DEBUG] ========== TSQ 메시지 스케줄링 완료 ==========\n");
	}
}

