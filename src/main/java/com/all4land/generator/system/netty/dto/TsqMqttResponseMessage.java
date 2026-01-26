package com.all4land.generator.system.netty.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/**
 * TSQ MQTT 송신 메시지 DTO
 * 예: [{"service":"uuid-1","service-size":"4005","destMMSI":["440123456","440654321"],"NMEA":"!AITSQ~~~"}]
 */
@Data
public class TsqMqttResponseMessage {
	private String service;           // 서비스 UUID
	
	@SerializedName("service-size")
	private String serviceSize;       // 서비스 크기
	
	@SerializedName("destMMSI")
	private List<String> destMMSI;    // 목적지 MMSI 리스트
	
	private String NMEA;              // NMEA 메시지
}
