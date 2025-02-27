package com.all4land.generator.ui.tab.ais.entity;

public class Range {
	//
	private double from;
	private double to;
	private int slotNumber;

	public Range(double from, double to, int slotNumber) {
        this.from = from;
        this.to = to;
        this.slotNumber = slotNumber;
    }

	public double getFrom() {
		return from;
	}

	public void setFrom(double from) {
		this.from = from;
	}

	public double getTo() {
		return to;
	}

	public void setTo(double to) {
		this.to = to;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	@Override
	public String toString() {
		return "Range{" +
                "from=" + from +
                ", to=" + to +
                ", slotNumber=" + slotNumber +
                '}';
	}
}
