package com.all4land.generator.ui.tab.ais.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;

import com.all4land.generator.system.netty.send.config.NettyServerTCPConfiguration;
import com.all4land.generator.system.netty.send.config.NettyServerUDPConfiguration;
import com.all4land.generator.ui.tab.ais.entity.event.change.TcpTargetClientAddRowEvent;
import com.all4land.generator.ui.tab.ais.entity.event.change.TcpTargetClientRemoveRowEvent;

import lombok.Data;

@Data
public class TcpServerTableEntity {
	//
	private final ApplicationEventPublisher eventPublisher;
	
	private String desc;
	private int port;
	private boolean status;
	private boolean selected;
	
	private NettyServerTCPConfiguration NettyServerTCPConfiguration;
	private List<TcpTargetClientInfoEntity> tcpTargetClientInfoEntitys = new ArrayList<>();
	
	public TcpServerTableEntity(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void addTcpTargetClientInfoEntity(TcpTargetClientInfoEntity tcpTargetClientInfoEntity) {
		//
		this.tcpTargetClientInfoEntitys.add(tcpTargetClientInfoEntity);
		
		if(this.selected) {
			TcpTargetClientAddRowEvent event = new TcpTargetClientAddRowEvent(this, tcpTargetClientInfoEntity);
			this.eventPublisher.publishEvent(event);
		}
		
		
	}
	
	public int getTcpTargetClientInfoEntityCount() {
		//
		return this.tcpTargetClientInfoEntitys.size();
	}
	
	public void removeTcpTargetClientInfoEntitys(String ip, int port) {
		//
		Iterator<TcpTargetClientInfoEntity> iterator = this.tcpTargetClientInfoEntitys.iterator();
		int index = 0; // 인덱스 추적용 변수
	    while (iterator.hasNext()) {
	    	TcpTargetClientInfoEntity tcpTargetClientTableEntity = iterator.next();
	        if (tcpTargetClientTableEntity.getIp().equals(ip) && tcpTargetClientTableEntity.getPort() == port) {
	            iterator.remove(); // 안전하게 삭제
	            System.out.println("tcpTargetClientTableEntity removed for IP: " + ip + ", Port: " + port);
	            
	            if(this.selected) {
	            	TcpTargetClientRemoveRowEvent event = new TcpTargetClientRemoveRowEvent(this, index);
		    		this.eventPublisher.publishEvent(event);
	            }
	            
	            
	            break; // 삭제된 후에는 루프를 더 이상 진행할 필요가 없으므로 루프 종료
	        }
	        index++; // 인덱스를 증가시켜 다음 요소로 이동
	    }
	}
}
