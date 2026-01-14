package com.all4land.generator.system.netty.send.config;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * UI 의존성 없는 간단한 TCP 서버 핸들러
 */
@Slf4j
public class SimpleTcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	// 연결된 클라이언트 정보 저장 (IP:Port -> Channel)
	private static final ConcurrentMap<String, Channel> connectedClients = new ConcurrentHashMap<>();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 클라이언트가 연결될 때 호출됩니다.
		Channel clientChannel = ctx.channel();
		InetSocketAddress remoteAddress = (InetSocketAddress) clientChannel.remoteAddress();
		String clientIP = remoteAddress.getAddress().getHostAddress();
		int clientPort = remoteAddress.getPort();
		String clientKey = clientIP + ":" + clientPort;
		
		connectedClients.put(clientKey, clientChannel);
		log.info("TCP 클라이언트 연결: {}:{}", clientIP, clientPort);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// 클라이언트로부터 메시지를 수신할 때 호출됩니다.
		String receivedMessage = msg.toString(CharsetUtil.UTF_8);
		log.info("TCP 메시지 수신: {}", receivedMessage);
		// 필요시 메시지 처리 로직 추가
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 예외가 발생할 때 호출됩니다.
		log.error("TCP 서버 예외 발생: {}", cause.getMessage(), cause);
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 클라이언트가 연결을 종료할 때 호출됩니다.
		Channel clientChannel = ctx.channel();
		InetSocketAddress remoteAddress = (InetSocketAddress) clientChannel.remoteAddress();
		if (remoteAddress != null) {
			String clientIP = remoteAddress.getAddress().getHostAddress();
			int clientPort = remoteAddress.getPort();
			String clientKey = clientIP + ":" + clientPort;
			connectedClients.remove(clientKey);
			log.info("TCP 클라이언트 연결 종료: {}:{}", clientIP, clientPort);
		}
	}
	
	/**
	 * 연결된 클라이언트 채널 가져오기
	 */
	public static Channel getClientChannel(String clientIP, int clientPort) {
		String clientKey = clientIP + ":" + clientPort;
		return connectedClients.get(clientKey);
	}
	
	/**
	 * 모든 연결된 클라이언트 채널 가져오기
	 */
	public static ConcurrentMap<String, Channel> getAllClients() {
		return connectedClients;
	}
}


