package com.all4land.generator.system.netty.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * TSQ 리소스 요청 메시지 DTO
 * 예: [{"serviceId":"uuid1","type":"0","size":"300000","sourceMmsi":"440115678","destMmsi":"440999999","testMmsi":"440123456"}, ...]
 */
@Data
public class TsqResourceRequestMessage {
	@SerializedName("serviceId")
	private String serviceId;    // 서비스 UUID
	private String type;         // 서비스 타입 ("0" 또는 "1")
	private String size;         // 용량 (문자열, 예: "300000", "100000")
	private String sourceMmsi;   // 송신 MMSI
	private String destMmsi;     // 수신 MMSI
	private String testMmsi;     // 테스트 MMSI (송신 토픽 및 destMMSI 리스트용)
}
