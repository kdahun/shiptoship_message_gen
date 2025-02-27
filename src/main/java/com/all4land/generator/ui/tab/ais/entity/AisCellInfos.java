package com.all4land.generator.ui.tab.ais.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AisCellInfos {
	//
	private long mmsi;
	private int slotTimeOut;
	private LocalDateTime channelATime;
	private LocalDateTime channelBTime;
	private MmsiEntity mmsiEntity;

	// 기본 생성자
	public AisCellInfos() {
	}

	// 복사 생성자
	public AisCellInfos(AisCellInfos other) {
		//
		this.mmsi = other.mmsi;
		this.slotTimeOut = other.slotTimeOut;
		this.channelATime = other.channelATime;
		this.channelBTime = other.channelBTime;
		this.mmsiEntity = other.mmsiEntity;
	}
}
