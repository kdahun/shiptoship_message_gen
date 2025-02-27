package com.all4land.generator.ui.tab.ais.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VdeUpperLegCellInfos {
	//
	private long mmsi;
	private LocalDateTime channelTime;

	// 기본 생성자
	public VdeUpperLegCellInfos() {
	}

	// 복사 생성자
	public VdeUpperLegCellInfos(VdeUpperLegCellInfos other) {
		//
		this.mmsi = other.mmsi;
		this.channelTime = other.channelTime;
	}
}
