package com.nsone.generator.ui.tab.ais.entity;

import lombok.Data;

@Data
public class UdpTargetClientTableEntity {
	//
	private String desc;
	private String ip;
	private int port;
	private boolean ais;
	private boolean asm;
	private boolean tsq;
	
	public Object[] rowData() {
		//
		Object[] newRow = { desc, ip, port, ais, asm, tsq };
		return newRow;
	}
}
