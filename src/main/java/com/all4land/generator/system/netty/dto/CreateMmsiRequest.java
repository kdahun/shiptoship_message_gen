package com.all4land.generator.system.netty.dto;

import java.util.List;

import lombok.Data;

/**
 * TCP 클라이언트로부터 받는 선박 생성 요청 JSON DTO
 */
@Data
public class CreateMmsiRequest {
	private String type;
	private List<MmsiData> data;
	
	@Data
	public static class MmsiData {
		private String regionId;
		private String mmsi;
		private double lat;
		private double lon;
		private int aisPeriod; // AIS 메시지 전송 주기 (초 단위)
	}
}

