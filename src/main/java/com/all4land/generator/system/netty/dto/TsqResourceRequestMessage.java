package com.all4land.generator.system.netty.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * TSQ 리소스 요청 메시지 DTO
 * 예: [{"serviceId":"uuid1","type":"0","size":"300000","sourceMmsi":"440115678","testMmsi":"440123456"}, ...]
 */
@Data
public class TsqResourceRequestMessage {
	@SerializedName("serviceId")
	private String serviceId;    // 서비스 UUID
	private String type;         // 서비스 타입 ("0" 또는 "1")
	private String size;         // 용량 (문자열, 예: "300000", "100000")
	private String sourceMmsi;   // 송신 MMSI
	private String testMmsi;     // 피시험 MMSI (송신 토픽 및 testMMSI 리스트용)
}
