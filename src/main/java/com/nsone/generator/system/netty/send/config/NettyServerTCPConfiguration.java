package com.nsone.generator.system.netty.send.config;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
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
			clientChannel.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
		} else {
			System.out.println("Failed to send message to client " + clientIP + ":" + clientPort);
		}
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
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
