package com.all4land.generator.system.netty.dto;

import java.util.List;

import lombok.Data;

/**
 * NMEA 스타일 제어 메시지 DTO
 * 예: $STG-02,{ships:[{mmsi:"440301234",state:"1"},{mmsi:"440301235",state:"0"}]}
 * 
 * 실제 JSON 부분만 파싱하므로 ShipsData만 포함
 */
@Data
public class ControlMessage {
	private String identifier; // STG-02 (파싱 시 사용)
	private List<ShipControl> ships;
	
	@Data
	public static class ShipControl {
		private String mmsi;
		private String state; // "0"=OFF, "1"=ON
		private List<String> testMmsi; // 피시험 MMSI 리스트
	}
}

