package com.all4land.generator.system.netty.send.config;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.quartz.Scheduler;

import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.system.component.ApplicationContextProvider;
import com.all4land.generator.system.netty.dto.CreateMmsiRequest;
import com.all4land.generator.system.netty.dto.ControlMessage;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * UI 의존성 없는 간단한 TCP 서버 핸들러
 * JSON 메시지를 받아서 선박 생성 등의 작업을 처리합니다.
 */
public class SimpleTcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	// 연결된 클라이언트 정보 저장 (IP:Port -> Channel)
	private static final ConcurrentMap<String, Channel> connectedClients = new ConcurrentHashMap<>();
	
	private static final Gson gson = new Gson();
	private static final Gson lenientGson = new com.google.gson.GsonBuilder()
			.setLenient()
			.create();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 클라이언트가 연결될 때 호출됩니다.
		Channel clientChannel = ctx.channel();
		InetSocketAddress remoteAddress = (InetSocketAddress) clientChannel.remoteAddress();
		String clientIP = remoteAddress.getAddress().getHostAddress();
		int clientPort = remoteAddress.getPort();
		String clientKey = clientIP + ":" + clientPort;
		
		connectedClients.put(clientKey, clientChannel);
		System.out.println("[DEBUG] TCP 클라이언트 연결: " + clientIP + ":" + clientPort);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// 클라이언트로부터 메시지를 수신할 때 호출됩니다.
		String receivedMessage = msg.toString(CharsetUtil.UTF_8);
		System.out.println("[DEBUG] TCP 메시지 수신: " + receivedMessage);
		
		// 메시지 타입 확인 및 처리
		try {
			// NMEA 스타일 메시지 체크 ($STG-02로 시작)
			if (receivedMessage.trim().startsWith("$STG-02")) {
				processNmeaControlMessage(receivedMessage);
			} else {
				// JSON 메시지 처리
				processJsonMessage(receivedMessage);
			}
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ 메시지 처리 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
			// 에러 응답 전송 (선택사항)
			String errorResponse = "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
			ctx.writeAndFlush(ctx.alloc().buffer().writeBytes(errorResponse.getBytes(CharsetUtil.UTF_8)));
		}
	}
	
	/**
	 * JSON 메시지를 파싱하고 타입에 따라 처리합니다.
	 */
	private void processJsonMessage(String jsonMessage) {
		try {
			CreateMmsiRequest request = gson.fromJson(jsonMessage, CreateMmsiRequest.class);
			
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
		} catch (JsonSyntaxException e) {
			System.out.println("[DEBUG] ❌ JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("JSON 파싱 실패: " + e.getMessage(), e);
		}
	}
	
	/**
	 * CREATE_MMSI 타입 메시지를 처리하여 선박을 생성합니다.
	 */
	private void handleCreateMmsi(CreateMmsiRequest request) {
		if (request.getData() == null || request.getData().isEmpty()) {
			System.out.println("[DEBUG] ⚠️ CREATE_MMSI 요청에 데이터가 없습니다.");
			return;
		}
		
		GlobalEntityManager globalEntityManager = ApplicationContextProvider.getApplicationContext().getBean(GlobalEntityManager.class);
		Scheduler scheduler = ApplicationContextProvider.getApplicationContext().getBean(Scheduler.class);
		QuartzCoreService quartzCoreService = ApplicationContextProvider.getApplicationContext().getBean(QuartzCoreService.class);
		
		System.out.println("[DEBUG] 선박 생성 시작 - 요청된 선박 수: " + request.getData().size());
		
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
				System.out.println("[DEBUG] ✅ 선박 생성 완료 - MMSI: " + mmsiData.getMmsi() + ", Region: " + mmsiData.getRegionId());
			} catch (IllegalArgumentException e) {
				// 중복된 MMSI나 유효하지 않은 MMSI 형식 등의 경우
				if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
					skipCount++;
					System.out.println("[DEBUG] ⚠️ 선박 생성 건너뜀 (중복 MMSI) - MMSI: " + mmsiData.getMmsi());
				} else {
					errorCount++;
					System.out.println("[DEBUG] ❌ 선박 생성 실패 - MMSI: " + mmsiData.getMmsi() + ", 오류: " + e.getMessage());
				}
			} catch (Exception e) {
				errorCount++;
				System.out.println("[DEBUG] ❌ 선박 생성 실패 - MMSI: " + mmsiData.getMmsi() + ", 오류: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		System.out.println("[DEBUG] 선박 생성 처리 완료 - 성공: " + successCount + ", 건너뜀: " + skipCount + ", 실패: " + errorCount);
	}
	
	/**
	 * NMEA 스타일 제어 메시지를 파싱하고 처리합니다.
	 * 포맷: $STG-02,{ships:[{mmsi:"440301234",state:"1"},{mmsi:"440301235",state:"0"}]}<CR><LF>
	 */
	private void processNmeaControlMessage(String nmeaMessage) {
		System.out.println("[DEBUG] NMEA 제어 메시지 수신: " + nmeaMessage);
		
		try {
			// NMEA 메시지 파싱
			// $STG-02,{...} 형식에서 JSON 부분 추출
			String jsonPart = extractJsonFromNmea(nmeaMessage);
			if (jsonPart == null || jsonPart.isEmpty()) {
				System.out.println("[DEBUG] ❌ NMEA 메시지에서 JSON 부분을 추출할 수 없음");
				return;
			}
			
			System.out.println("[DEBUG] 추출된 JSON: " + jsonPart);
			
			// JSON 파싱 (ships 배열만 포함된 JSON)
			// 예: {ships:[{mmsi:"440301234",state:"1"}]}
			// lenient 모드로 파싱하여 더 유연하게 처리
			ControlMessage controlMessage = lenientGson.fromJson(jsonPart, ControlMessage.class);
			
			if (controlMessage == null || controlMessage.getShips() == null || controlMessage.getShips().isEmpty()) {
				System.out.println("[DEBUG] ⚠️ 제어 메시지에 ships 데이터가 없습니다.");
				return;
			}
			
			// STG-02 메시지 처리
			handleControlMessage(controlMessage);
		} catch (JsonSyntaxException e) {
			System.out.println("[DEBUG] ❌ NMEA 메시지 JSON 파싱 오류: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("NMEA 메시지 파싱 실패: " + e.getMessage(), e);
		}
	}
	
	/**
	 * NMEA 메시지에서 JSON 부분을 추출합니다.
	 * 예: $STG-02,{ships:[...]} -> {ships:[...]}
	 */
	private String extractJsonFromNmea(String nmeaMessage) {
		// $STG-02, 이후 부분 찾기
		int commaIndex = nmeaMessage.indexOf(',');
		if (commaIndex == -1) {
			return null;
		}
		
		// 콤마 이후 부분 추출
		String jsonPart = nmeaMessage.substring(commaIndex + 1);
		
		// <CR><LF> 리터럴 문자열 제거 (사용자가 실제로 <CR><LF> 문자열을 보낸 경우)
		jsonPart = jsonPart.replace("<CR><LF>", "");
		jsonPart = jsonPart.replace("<CR>", "");
		jsonPart = jsonPart.replace("<LF>", "");
		
		// 실제 CRLF 문자 제거 (\r\n, \r, \n)
		jsonPart = jsonPart.replaceAll("[\r\n]+", "");
		
		// 앞뒤 공백 제거
		jsonPart = jsonPart.trim();
		
		// JSON 끝부분의 불필요한 문자 제거 (예: }, }], 등 뒤에 오는 문자)
		// JSON이 }로 끝나야 하므로 } 이후의 모든 문자 제거
		int lastBraceIndex = jsonPart.lastIndexOf('}');
		if (lastBraceIndex != -1 && lastBraceIndex < jsonPart.length() - 1) {
			jsonPart = jsonPart.substring(0, lastBraceIndex + 1);
		}
		
		return jsonPart;
	}
	
	/**
	 * STG-02 제어 메시지를 처리하여 MMSI별 메시지 생성 상태를 제어합니다.
	 */
	private void handleControlMessage(ControlMessage controlMessage) {
		GlobalEntityManager globalEntityManager = ApplicationContextProvider.getApplicationContext().getBean(GlobalEntityManager.class);
		
		System.out.println("[DEBUG] 제어 메시지 처리 시작 - 선박 수: " + controlMessage.getShips().size());
		
		int successCount = 0;
		int notFoundCount = 0;
		int errorCount = 0;
		
		for (ControlMessage.ShipControl shipControl : controlMessage.getShips()) {
			try {
				long mmsi = Long.parseLong(shipControl.getMmsi());
				String state = shipControl.getState();
				
				boolean success = globalEntityManager.controlMmsiState(mmsi, state);
				if (success) {
					successCount++;
					System.out.println("[DEBUG] ✅ 상태 제어 완료 - MMSI: " + mmsi + ", State: " + state);
				} else {
					notFoundCount++;
					System.out.println("[DEBUG] ⚠️ 선박을 찾을 수 없음 - MMSI: " + mmsi);
				}
			} catch (NumberFormatException e) {
				errorCount++;
				System.out.println("[DEBUG] ❌ 유효하지 않은 MMSI 형식: " + shipControl.getMmsi());
			} catch (Exception e) {
				errorCount++;
				System.out.println("[DEBUG] ❌ 상태 제어 실패 - MMSI: " + shipControl.getMmsi() + ", 오류: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		System.out.println("[DEBUG] 제어 메시지 처리 완료 - 성공: " + successCount + ", 찾을 수 없음: " + notFoundCount + ", 실패: " + errorCount);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 예외가 발생할 때 호출됩니다.
		System.out.println("[DEBUG] ❌ TCP 서버 예외 발생: " + cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 클라이언트가 연결을 종료할 때 호출됩니다.
		Channel clientChannel = ctx.channel();
		InetSocketAddress remoteAddress = (InetSocketAddress) clientChannel.remoteAddress();
		if (remoteAddress != null) {
			String clientIP = remoteAddress.getAddress().getHostAddress();
			int clientPort = remoteAddress.getPort();
			String clientKey = clientIP + ":" + clientPort;
			connectedClients.remove(clientKey);
			System.out.println("[DEBUG] TCP 클라이언트 연결 종료: " + clientIP + ":" + clientPort);
		}
	}
	
	/**
	 * 연결된 클라이언트 채널 가져오기
	 */
	public static Channel getClientChannel(String clientIP, int clientPort) {
		String clientKey = clientIP + ":" + clientPort;
		return connectedClients.get(clientKey);
	}
	
	/**
	 * 모든 연결된 클라이언트 채널 가져오기
	 */
	public static ConcurrentMap<String, Channel> getAllClients() {
		return connectedClients;
	}
}


