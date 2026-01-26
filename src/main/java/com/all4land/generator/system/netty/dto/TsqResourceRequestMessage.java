package com.all4land.generator.system.netty.dto;

import lombok.Data;

/**
 * TSQ 리소스 요청 메시지 DTO
 * 예: [{"service":"uuid1","type":"0","size":"300000","sourceMmsi":"440115678","destMmsi":"440999999"}, ...]
 */
@Data
public class TsqResourceRequestMessage {
	private String service;      // 서비스 UUID
	private String type;         // 서비스 타입 ("0" 또는 "1")
	private String size;         // 용량 (문자열, 예: "300000", "100000")
	private String sourceMmsi;   // 송신 MMSI
	private String destMmsi;     // 수신 MMSI
}
