package com.nsone.generator.system.netty.send.config;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.nsone.generator.system.util.NetWorkUtil;
import com.nsone.generator.ui.tab.ais.entity.UdpServerTableEntity;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerUDPConfiguration implements AutoCloseable {
	//
	private int port;
	private NioDatagramChannel udpServerChannel;
	private Bootstrap udpBootstrap;
	private NioEventLoopGroup bossGroup;

    private NetworkInterface networkInterface;
    private InetSocketAddress multicastInfo;
    private NettyUdpServerConfig nettyUdpServerConfig;
	
	public NioEventLoopGroup udpServerBossGroup() {
		if (this.bossGroup == null) {
			this.bossGroup = new NioEventLoopGroup(1);
		}
		return this.bossGroup;
	}

	public Bootstrap udpServerBootstrap(NettyUdpServerConfig nettyUdpServerConfig) {
		//
		this.networkInterface = NetWorkUtil.findNetworkInterface();
		
		UdpServerTableEntity udpServerTableEntity = nettyUdpServerConfig.getUdpServerTableEntity();
		String[] multiCastInfo = udpServerTableEntity.getMultiCast().split(":");
		
		this.multicastInfo = new InetSocketAddress(multiCastInfo[0], Integer.valueOf(multiCastInfo[1]));
		
		this.nettyUdpServerConfig = nettyUdpServerConfig;
		this.udpBootstrap = new Bootstrap();
		this.udpBootstrap.group(this.udpServerBossGroup())
		.channel(NioDatagramChannel.class)
//		.localAddress("192.0.0.5", 50001)
//		.option(ChannelOption.SO_BROADCAST, true)
        .option(ChannelOption.SO_REUSEADDR, true)
        .option(ChannelOption.IP_MULTICAST_IF, this.networkInterface)
        .option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, false) // Disable multicast loopback
        .option(ChannelOption.IP_MULTICAST_TTL, 255)
//		.option(ChannelOption.SO_REUSEADDR, true)
				.handler(new ChannelInitializer<DatagramChannel>() {
					@Override
					public void initChannel(DatagramChannel ch) throws Exception {
						//
	                    ch.pipeline().addLast(new UdpServerHandler(nettyUdpServerConfig));
					}
				});

//		this.udpBootstrap.option(ChannelOption.SO_BROADCAST, true);
//		this.udpBootstrap.option(ChannelOption.SO_REUSEADDR, true);
		
//		this.udpBootstrap.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true); // Disable multicast loopback
//		this.udpBootstrap.option(ChannelOption.IP_MULTICAST_IF, this.networkInterface);
//		this.udpBootstrap.option(ChannelOption.IP_MULTICAST_TTL, 255);

		try {
			NioDatagramChannel ch = (NioDatagramChannel) this.udpBootstrap.bind(Integer.valueOf(multiCastInfo[1])).sync().channel();
			this.udpServerChannel = ch;
			log.info("boot config started.....");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.udpBootstrap;
	}

	public boolean startUdp(int port) {
		//
		try {
			//
			if(this.nettyUdpServerConfig.getUdpServerTableEntity().getMultiCast() != null
					&& !this.nettyUdpServerConfig.getUdpServerTableEntity().getMultiCast().isBlank()
					&& !this.nettyUdpServerConfig.getUdpServerTableEntity().getMultiCast().isEmpty()) {
				//
//				UdpServerTableEntity udpServerTableEntity = this.nettyUdpServerConfig.getUdpServerTableEntity();
//				String[] multiCastInfo = udpServerTableEntity.getMultiCast().split(":");
//				
//				this.multicastInfo = new InetSocketAddress(multiCastInfo[0], Integer.valueOf(multiCastInfo[1]));
				
//				this.findNetworkInterface();
				this.port = port;
//				this.udpServerChannel = this.udpBootstrap.bind(port).sync().channel();
//				this.udpServerChannel = (NioDatagramChannel) this.udpBootstrap.bind(port).sync().channel();
				
				
				// 멀티캐스트 그룹 가입 및 옵션 설정
//	            NioDatagramChannel ndc = (NioDatagramChannel) this.udpServerChannel;
//	            channel.config().setOption(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true); // 루프백 비활성화
//	            channel.config().setOption(ChannelOption.IP_MULTICAST_IF, this.networkInterface);
//	            channel.config().setOption(ChannelOption.IP_MULTICAST_TTL, 255);
				udpServerChannel.joinGroup(multicastInfo, this.networkInterface).sync();
	            
//	            ndc.joinGroup(new InetSocketAddress(multiCastInfo[0], Integer.valueOf(multiCastInfo[1]), this.networkInterface).sync();
	            
	            log.info("UDP Multi Cast Server IP: {}, PORT: {} Ready OK.", multicastInfo);
			}else {
				//
				this.port = port;
				this.udpServerChannel = (NioDatagramChannel) this.udpBootstrap.bind(port).sync().channel();
				
	            log.info("UDP Server port: {} Ready OK.", this.port);
			}
			return true;
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			return false;
		}
	}

	public void sendToClient(String clientIP, int clientPort, String message) {
		//
		if (this.udpServerChannel != null && this.udpServerChannel.isActive()) {
			//
			DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8),
                    new InetSocketAddress(clientIP, clientPort));
            this.udpServerChannel.writeAndFlush(packet).addListener(future -> {
//                if (future.isSuccess()) {
//                    log.info("Message sent to client {}:{}", clientIP, clientPort);
//                } else {
//                    log.error("Failed to send message to client {}:{} - {}", clientIP, clientPort, future.cause().getMessage());
//                }
            });
		} else {
			System.out.println("Failed to send message to client " + clientIP + ":" + clientPort);
		}
	}
	
	public void sendToClient(String message) {
		//
		if (this.udpServerChannel != null && this.udpServerChannel.isActive()) {
			//
			DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8),this.multicastInfo);
            this.udpServerChannel.writeAndFlush(packet);
		} else {
			System.out.println("Failed to send message to client " + "239.255.27.1" + ":" + this.port);
		}
	}

	
	
	public InetSocketAddress getMultiCastInfo() {
		//
		return this.multicastInfo;
	}
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		try {
			if (this.udpServerChannel != null) {
				//// 멀티캐스트 그룹 탈퇴
				
				if(this.multicastInfo != null) {
					//
					((NioDatagramChannel) this.udpServerChannel).leaveGroup(this.multicastInfo, this.networkInterface).sync();
				}
				this.udpServerChannel.close().sync();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			if (this.bossGroup != null) {
				this.bossGroup.shutdownGracefully();
				this.bossGroup = null; // 그룹을 해제하여 재생성 가능하게 함
			}
		}
		log.info("UDP Server : Port : {} Destroyed. Left multicast group: {}", this.port, this.multicastInfo);
	}
}
