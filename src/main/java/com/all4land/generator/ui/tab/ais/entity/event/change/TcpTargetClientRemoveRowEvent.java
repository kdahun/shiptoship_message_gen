package com.all4land.generator.ui.tab.ais.entity.event.change;

import org.springframework.context.ApplicationEvent;

import com.all4land.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;

public class TcpTargetClientRemoveRowEvent extends ApplicationEvent {
	//
	private static final long serialVersionUID = 1383480857688226143L;

	private int row;
	
	public TcpTargetClientRemoveRowEvent(Object source, int row) {
		super(source);
		// TODO Auto-generated constructor stub
		this.row = row;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

}
