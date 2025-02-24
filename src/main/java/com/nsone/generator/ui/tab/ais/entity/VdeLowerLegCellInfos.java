package com.nsone.generator.ui.tab.ais.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VdeLowerLegCellInfos {
	//
	private long mmsi;
	private LocalDateTime channelTime;

	// 기본 생성자
	public VdeLowerLegCellInfos() {
	}

	// 복사 생성자
	public VdeLowerLegCellInfos(VdeLowerLegCellInfos other) {
		//
		this.mmsi = other.mmsi;
		this.channelTime = other.channelTime;
	}
}
