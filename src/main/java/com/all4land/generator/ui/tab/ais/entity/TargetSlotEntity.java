package com.all4land.generator.ui.tab.ais.entity;

import org.springframework.stereotype.Component;

@Component
public class TargetSlotEntity {
	//
	private double ssSSSS;
	private int slotNumber;
	private boolean channel;
	private int row;
	private int column;

	public double getSsSSSS() {
		return ssSSSS;
	}

	public void setSsSSSS(double ssSSSS) {
		this.ssSSSS = ssSSSS;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public boolean isChannel() {
		return channel;
	}

	public void setChannel(boolean channel) {
		this.channel = channel;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

}
