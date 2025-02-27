package com.all4land.generator.ui.tab.ais.entity;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class TcpTargetClientInfoEntity {
	//
	private String desc;
	private String ip;
	private int port;
	private boolean ais;
	private boolean asm;
	private boolean tsq;
	private Channel clientChannel;
	
	public Object[] rowData() {
		//
		Object[] newRow = { desc, ip, port, ais, asm, tsq };
		return newRow;
	}
	
}
