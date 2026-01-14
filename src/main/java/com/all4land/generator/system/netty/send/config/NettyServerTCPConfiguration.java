package com.all4land.generator.system.netty.send.config;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerTCPConfiguration implements AutoCloseable {
	//
	private int port;
	private Channel tcpServerChannel;
	private ServerBootstrap serverBootstrap;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;

	public NioEventLoopGroup tcpServerBossGroup() {
		if (this.bossGroup == null) {
			this.bossGroup = new NioEventLoopGroup(1);
		}
		return this.bossGroup;
	}

	public NioEventLoopGroup tcpServerWorkerGroup() {
		//
		if (this.workerGroup == null) {
			this.workerGroup = new NioEventLoopGroup(1);
		}
		return this.workerGroup;
	}

	public ServerBootstrap tcpServerBootstrap(NettyTcpServerConfig nettyServerConfig) {
		//
		this.serverBootstrap = new ServerBootstrap();
		this.serverBootstrap.group(this.tcpServerBossGroup(), this.tcpServerWorkerGroup()).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						//
						ch.pipeline().addLast(new TcpServerHandler(nettyServerConfig));
					}
				});

		this.serverBootstrap.option(ChannelOption.SO_BACKLOG, 1);
		this.serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		this.serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		this.serverBootstrap.childOption(ChannelOption.SO_REUSEADDR, true);

		return this.serverBootstrap;
	}

	/**
	 * UI 의존성 없는 간단한 TCP 서버 Bootstrap 생성
	 */
	public ServerBootstrap tcpServerBootstrapSimple() {
		//
		this.serverBootstrap = new ServerBootstrap();
		this.serverBootstrap.group(this.tcpServerBossGroup(), this.tcpServerWorkerGroup()).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						//
						ch.pipeline().addLast(new SimpleTcpServerHandler());
					}
				});

		this.serverBootstrap.option(ChannelOption.SO_BACKLOG, 1);
		this.serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		this.serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		this.serverBootstrap.childOption(ChannelOption.SO_REUSEADDR, true);

		return this.serverBootstrap;
	}

	public boolean startTcp(int port) {
		//
		try {
			//
			this.port = port;
			this.tcpServerChannel = this.serverBootstrap.bind(new InetSocketAddress(port)).sync().channel();
			log.info("TCP Server port: {} Ready OK.", this.port);
			return true;
		} catch (InterruptedException e) {
			//
			log.error(e.getMessage());
			return false;
		}
	}

	public void sendToClient(Channel clientChannel, String clientIP, int clientPort, String message) {
		//
		if (clientChannel != null && clientChannel.isActive()) {
			//
			log.info("[DEBUG] NettyServerTCPConfiguration.sendToClient - 전송 시작: {}:{}", clientIP, clientPort);
			log.info("[DEBUG] 메시지 내용 (처음 100자): {}", message.length() > 100 ? message.substring(0, 100) + "..." : message);
			clientChannel.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
			log.info("[DEBUG] ✅ NettyServerTCPConfiguration.sendToClient - 전송 완료: {}:{}", clientIP, clientPort);
		} else {
			log.warn("[DEBUG] ❌ NettyServerTCPConfiguration.sendToClient - 채널이 null이거나 비활성: {}:{} (channel: {}, isActive: {})", 
					clientIP, clientPort, clientChannel != null, clientChannel != null ? clientChannel.isActive() : false);
			System.out.println("Failed to send message to client " + clientIP + ":" + clientPort);
		}
	}

	@Override
	public void close() throws Exception {
		try {
			if (this.tcpServerChannel != null) {
				this.tcpServerChannel.close().sync();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			if (this.bossGroup != null) {
				this.bossGroup.shutdownGracefully();
				this.bossGroup = null; // 그룹을 해제하여 재생성 가능하게 함
			}
			if (this.workerGroup != null) {
				this.workerGroup.shutdownGracefully();
				this.workerGroup = null; // 그룹을 해제하여 재생성 가능하게 함
			}
		}
		log.info("TCP Server : Port : {} close connection.", this.port);
	}
}
