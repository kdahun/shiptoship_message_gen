package com.all4land.generator.system.netty.dto;

import java.util.List;

import lombok.Data;

/**
 * ASM 상태 제어 메시지 DTO
 * 예: [{"serviceId":"s2234567","sourceMmsi":"440301234","testMmsi":["440123456"],"state":"1","size":"3","asmPeriod":"30"}, ...]
 * asmPeriod: "0"=단발 메시지, "4"~"360"=초 단위 주기
 */
@Data
public class AsmControlMessage {
	private String identifier; // STG-02 (파싱 시 사용)
	private List<AsmShipControl> ships;
	
	@Data
	public static class AsmShipControl {
		private String serviceId;     // 서비스 식별자
		private String sourceMmsi;    // 송신 MMSI (기존 mmsi)
		private List<String> testMmsi; // 피시험 대상 MMSI 배열
		private String state;         // "0"=OFF, "1"=ON
		private String size;          // "1"~"3" (슬롯 점유 개수)
		private String asmPeriod;     // "0"=단발 메시지, "4"~"360"=초 단위 주기
	}
}

