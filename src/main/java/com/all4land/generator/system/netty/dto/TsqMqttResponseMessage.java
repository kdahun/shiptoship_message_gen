package com.all4land.generator.system.netty.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * TSQ MQTT 송신 메시지 DTO
 * 예: [{"service":"uuid-1","serviceSize":"4005","testMmsi":"440123456","NMEA":"!AITSQ~~~"}]
 */
@Data
public class TsqMqttResponseMessage {
	@SerializedName("serviceId")
	private String service;           // 서비스 UUID
	
	@SerializedName("serviceSize")
	private String serviceSize;       // 서비스 크기
	
	@SerializedName("testMmsi")
	private String testMmsi;          // 피시험 MMSI
	
	private String NMEA;              // NMEA 메시지
}
