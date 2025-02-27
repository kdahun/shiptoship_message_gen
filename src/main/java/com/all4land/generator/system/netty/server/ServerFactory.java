package com.all4land.generator.system.netty.server;

import org.springframework.stereotype.Service;

import com.all4land.generator.system.netty.send.config.NettyServerTCPConfiguration;
import com.all4land.generator.system.netty.send.config.NettyServerUDPConfiguration;
import com.all4land.generator.system.netty.send.config.NettyTcpServerConfig;
import com.all4land.generator.system.netty.send.config.NettyUdpServerConfig;
import com.all4land.generator.system.util.BeanUtils;
import com.all4land.generator.ui.tab.ais.entity.TcpServerTableEntity;
import com.all4land.generator.ui.tab.ais.entity.UdpServerTableEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ServerFactory {
	//
	private final static String TCP_NAME = "_TcpServer";
	private final static String UDP_NAME = "_UdpServer";
	
	public boolean makeTcpServer(TcpServerTableEntity entity, NettyTcpServerConfig nettyServerConfig) {
		//
		NettyServerTCPConfiguration configuration = null;
		try {
			//
			configuration = (NettyServerTCPConfiguration) BeanUtils.getBean(entity.getPort() + TCP_NAME);
		} catch (Exception e) {
			// TODO: handle exception
			log.info("{} 포트로 만들어진 TCP 서버가 없다.", entity.getPort());
		}
		
		if (configuration == null) {
			//
			log.info("{} 포트 TCP Server 만든다.", entity.getPort());
			BeanUtils.registerBean(entity.getPort() + TCP_NAME, NettyServerTCPConfiguration.class);
			configuration = (NettyServerTCPConfiguration) BeanUtils.getBean(entity.getPort() + TCP_NAME);
		}
		
		configuration.tcpServerBootstrap(nettyServerConfig);
		entity.setNettyServerTCPConfiguration(configuration);
		return configuration.startTcp(entity.getPort());
	}
	
	public boolean makeUdpServer(UdpServerTableEntity entity, NettyUdpServerConfig nettyUdpServerConfig) {
		//
		NettyServerUDPConfiguration configuration = null;
		try {
			//
			configuration = (NettyServerUDPConfiguration) BeanUtils.getBean(entity.getPort() + UDP_NAME);
		} catch (Exception e) {
			// TODO: handle exception
			log.info("{} 포트로 만들어진 UDP 서버가 없다.", entity.getPort());
		}
		
		if (configuration == null) {
			//
			log.info("{} 포트 UDP Server 만든다.", entity.getPort());
			BeanUtils.registerBean(entity.getPort() + UDP_NAME, NettyServerUDPConfiguration.class);
			configuration = (NettyServerUDPConfiguration) BeanUtils.getBean(entity.getPort() + UDP_NAME);
		}
		
		configuration.udpServerBootstrap(nettyUdpServerConfig);
		entity.setNettyServerUDPConfiguration(configuration);
		return configuration.startUdp(entity.getPort());
	}
	
	public void removeTCPServer(TcpServerTableEntity entity) {
		//
		if(entity.getNettyServerTCPConfiguration() != null) {
			entity.setNettyServerTCPConfiguration(null);
			BeanUtils.removeBean(entity.getPort() + TCP_NAME);
		}
	}
	
	public void removeUDPServer(UdpServerTableEntity entity) {
		//
		if(entity.getNettyServerUDPConfiguration() != null) {
			entity.setNettyServerUDPConfiguration(null);
			BeanUtils.removeBean(entity.getPort() + UDP_NAME);
			
			log.info("{} 포트 UDP Server Close.", entity.getPort());
		}
	}
}
