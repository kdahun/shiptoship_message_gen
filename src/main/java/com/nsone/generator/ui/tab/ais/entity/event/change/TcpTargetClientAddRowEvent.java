package com.nsone.generator.ui.tab.ais.entity.event.change;

import org.springframework.context.ApplicationEvent;

import com.nsone.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;

public class TcpTargetClientAddRowEvent extends ApplicationEvent {
	//
	private static final long serialVersionUID = 1383480857688226143L;

	private TcpTargetClientInfoEntity tcpTargetClientInfoEntity;
	
	public TcpTargetClientAddRowEvent(Object source, TcpTargetClientInfoEntity tcpTargetClientInfoEntity) {
		super(source);
		// TODO Auto-generated constructor stub
		this.tcpTargetClientInfoEntity = tcpTargetClientInfoEntity;
	}
	//

	public TcpTargetClientInfoEntity getTcpTargetClientInfoEntity() {
		return tcpTargetClientInfoEntity;
	}

	public void setTcpTargetClientInfoEntity(TcpTargetClientInfoEntity tcpTargetClientInfoEntity) {
		this.tcpTargetClientInfoEntity = tcpTargetClientInfoEntity;
	}
	

}
