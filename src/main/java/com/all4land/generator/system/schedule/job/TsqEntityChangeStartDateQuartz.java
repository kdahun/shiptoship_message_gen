package com.all4land.generator.system.schedule.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.all4land.generator.ais.ESIMessageUtil;
import com.all4land.generator.ais.TerrestrialSlotResourceRequest;
import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.SlotStateManager;
import com.all4land.generator.system.component.ApplicationContextProvider;
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.system.mqtt.MqttClientConfiguration;
import com.all4land.generator.system.mqtt.MqttMessageProcessor;
import com.all4land.generator.system.netty.dto.TsqMqttResponseMessage;
import com.all4land.generator.system.netty.dto.TsqResourceRequestMessage;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("tsqEntityChangeStartDateQuartz")
public class TsqEntityChangeStartDateQuartz implements Job {
	
	private final MqttClientConfiguration mqttClient;
	private final GlobalEntityManager globalEntityManager;
	private final VirtualTimeManager virtualTimeManager;
	private final SlotStateManager slotStateManager;
	private final Gson gson = new Gson();
	private static final AtomicInteger tsqSeq = new AtomicInteger(1);
	
	public TsqEntityChangeStartDateQuartz(
			MqttClientConfiguration mqttClient,
			GlobalEntityManager globalEntityManager,
			VirtualTimeManager virtualTimeManager,
			SlotStateManager slotStateManager) {
		this.mqttClient = mqttClient;
		this.globalEntityManager = globalEntityManager;
		this.virtualTimeManager = virtualTimeManager;
		this.slotStateManager = slotStateManager;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("[DEBUG] ========== TsqEntityChangeStartDateQuartz.execute() 시작 ==========");
		
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		TsqResourceRequestMessage request = (TsqResourceRequestMessage) jobDataMap.get("tsqRequest");
		Integer slotNumber = (Integer) jobDataMap.get("slotNumber");
		
		if (request == null) {
			System.out.println("[DEBUG] ❌ TSQ 요청이 없습니다.");
			return;
		}
		
		if (slotNumber == null) {
			System.out.println("[DEBUG] ❌ 슬롯 번호가 없습니다.");
			return;
		}
		
		System.out.println("[DEBUG] TSQ Job 실행 - Service: " + request.getServiceId() + 
				", SlotNumber: " + slotNumber);
		
		try {
			// 가상 시간 사용
			LocalDateTime virtualTime = virtualTimeManager.getCurrentVirtualTime();
			
			// TDMA 프레임 계산 (slotNumber / 90)
			int tdmaFrame = slotNumber / 90;
			
			// GlobalEntityManager에서 UUID 값 가져오기
			String shoreStationId = globalEntityManager.getUuid();
			
			// 시퀀스 번호 증가
			int seq = tsqSeq.getAndIncrement();
			if (seq >= 1000) {
				tsqSeq.set(1);
				seq = 1;
			}
			
			// ESI 메시지 생성에 필요한 파라미터
			String linkId = "19";
			String channelLeg = "0";
			String tdmachannel = "0";
			String totalAccountSlot = "14";
			String firstSlotNumber = String.valueOf(slotNumber);
			String physicalChannelNumber = "0"; // TSQ 메시지 생성에 사용
			
			// ESI 메시지 생성
			ESIMessageUtil esi = new ESIMessageUtil(shoreStationId, channelLeg, String.valueOf(tdmaFrame), 
					tdmachannel, totalAccountSlot, firstSlotNumber, linkId);
			String esiMessage = esi.getMessage();
			
			// TSQ NMEA 메시지 생성
			String tsqNmeaMessage = TerrestrialSlotResourceRequest.getTerrestrialSlotResourceRequestNewFormat(
					String.valueOf(seq), request.getSourceMmsi(), request.getTestMmsi(), physicalChannelNumber, linkId);
			
			// NMEA 필드에 TSQ 메시지 + CRLF + ESI 메시지 조립
			StringBuilder nmeaBuilder = new StringBuilder();
			nmeaBuilder.append(tsqNmeaMessage).append(SystemConstMessage.CRLF);
			nmeaBuilder.append(esiMessage);
			String nmeaWithEsi = nmeaBuilder.toString();
			
			// MQTT로 전송
			if (mqttClient != null && mqttClient.isConnected()) {
				try {
					// MQTT 토픽 형식: mg/ms/tsq/{testMmsi}/{yyyyMMddHHmmss.SSSS}/ (AIS/ASM과 동일하게 ms 단위에 . 포함)
					// 가상 시간 사용
					DateTimeFormatter topicFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSS");
					String timestamp = virtualTime.format(topicFormatter);
					String mqttTopic = "mg/ms/tsq/" + request.getSourceMmsi() + "/" + timestamp + "/";
					
					// MQTT 송신용 DTO 생성
					TsqMqttResponseMessage mqttResponse = new TsqMqttResponseMessage();
					mqttResponse.setService(request.getServiceId());
					mqttResponse.setServiceSize(request.getSize());
					mqttResponse.setTestMmsi(request.getTestMmsi());
					mqttResponse.setNMEA(nmeaWithEsi); // TSQ + CRLF + ESI 포함
					
					// JSON 배열로 변환
					List<TsqMqttResponseMessage> mqttResponseList = Arrays.asList(mqttResponse);
					String mqttMessage = gson.toJson(mqttResponseList);
					
					mqttClient.publish(mqttTopic, mqttMessage, 1, false);
					
					System.out.println("[DEBUG] ✅ TSQ 메시지 MQTT 전송 완료 - Service: " + request.getServiceId() + 
							", Topic: " + mqttTopic + ", SlotNumber: " + slotNumber);
					System.out.println("[DEBUG] 전송된 메시지:\n" + mqttMessage);
					
					// 전송 후 다음 큐 메시지 처리
					// ApplicationContext를 통해 MqttMessageProcessor 조회 (순환 참조 방지)
					try {
						MqttMessageProcessor mqttMessageProcessor = ApplicationContextProvider
								.getApplicationContext()
								.getBean("mqttMessageProcessor", MqttMessageProcessor.class);
						if (mqttMessageProcessor != null) {
							mqttMessageProcessor.processTsqQueue();
						}
					} catch (Exception e) {
						System.out.println("[DEBUG] ⚠️ MqttMessageProcessor 조회 실패: " + e.getMessage());
					}
					
				} catch (Exception e) {
					System.out.println("[DEBUG] ❌ TSQ 메시지 MQTT 전송 중 오류: " + e.getMessage());
					e.printStackTrace();
					// 전송 실패 시 슬롯 해제
					slotStateManager.releaseSlot(slotNumber);
				}
			} else {
				System.out.println("[DEBUG] ⚠️ MQTT 클라이언트가 연결되지 않았거나 사용할 수 없습니다.");
				// MQTT 클라이언트가 없으면 슬롯 해제
				slotStateManager.releaseSlot(slotNumber);
			}
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ TSQ Job 실행 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
			// 오류 발생 시 슬롯 해제
			slotStateManager.releaseSlot(slotNumber);
		}
		
		System.out.println("[DEBUG] ========== TsqEntityChangeStartDateQuartz.execute() 종료 ==========");
	}
}
