package com.all4land.generator.ui.tab.ais.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AsmCellInfos {
	//
	private long mmsi;
	private LocalDateTime time;

	// 기본 생성자
	public AsmCellInfos() {
	}

	// 복사 생성자
	public AsmCellInfos(AsmCellInfos other) {
		//
		this.mmsi = other.mmsi;
		this.time = other.time;
	}
}
