package com.nsone.generator.ui.tab.ais.entity;

import java.awt.Color;

import lombok.Data;

@Data
public class CellInfos {
	//
	private int slotNumber;
	private Color color;
	private Color defaultColor;
	private AisCellInfos aisCellInfos;
	private AsmCellInfos asmCellInfosForA;
	private AsmCellInfos asmCellInfosForB;

	private VdeLowerLegCellInfos vdeLowerLegCellInfos;
	private VdeUpperLegCellInfos vdeUpperLegCellInfos;

	// 기본 생성자
	public CellInfos() {
		//
	}

	// 복사 생성자
	public CellInfos(CellInfos other) {
		//
		this.slotNumber = other.slotNumber;
		this.color = other.color;
		this.defaultColor = other.defaultColor;

		// 참조 타입 필드에 대해 깊은 복사 수행
		this.aisCellInfos = (other.aisCellInfos != null) ? new AisCellInfos(other.aisCellInfos) : null;

		this.asmCellInfosForA = (other.asmCellInfosForA != null) ? new AsmCellInfos(other.asmCellInfosForA) : null;
		this.asmCellInfosForB = (other.asmCellInfosForB != null) ? new AsmCellInfos(other.asmCellInfosForB) : null;

		this.vdeLowerLegCellInfos = (other.vdeLowerLegCellInfos != null)
				? new VdeLowerLegCellInfos(other.vdeLowerLegCellInfos)
				: null;
		this.vdeUpperLegCellInfos = (other.vdeUpperLegCellInfos != null)
				? new VdeUpperLegCellInfos(other.vdeUpperLegCellInfos)
				: null;
	}
}
