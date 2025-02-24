package com.nsone.generator.ui.tab.ais.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nsone.generator.system.netty.send.config.NettyServerUDPConfiguration;

import lombok.Data;

@Data
public class UdpServerTableEntity {
	//
	private String desc;
	private String multiCast;
	private int port;
	private boolean status;
	
	private NettyServerUDPConfiguration nettyServerUDPConfiguration;
	private List<UdpTargetClientTableEntity> udpTargetClientTableEntitys = new ArrayList<>();
	
	public void addUdpTargetClientTableEntity(UdpTargetClientTableEntity udpTargetClientTableEntity) {
		//
		this.udpTargetClientTableEntitys.add(udpTargetClientTableEntity);
	}
	
	public int getUdpTargetClientTableEntityCount() {
		//
		return this.udpTargetClientTableEntitys.size();
	}
	
	public void removeUdpTargetClientTableEntity(String ip, int port) {
		//
		Iterator<UdpTargetClientTableEntity> iterator = this.udpTargetClientTableEntitys.iterator();
	    while (iterator.hasNext()) {
	    	UdpTargetClientTableEntity udpTargetClientTableEntity = iterator.next();
	        if (udpTargetClientTableEntity.getIp().equals(ip) && udpTargetClientTableEntity.getPort() == port) {
	            iterator.remove(); // 안전하게 삭제
	            System.out.println("udpTargetClientTableEntity removed for IP: " + ip + ", Port: " + port);
	            break; // 삭제된 후에는 루프를 더 이상 진행할 필요가 없으므로 루프 종료
	        }
	    }
	}
}
