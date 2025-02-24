//package com.nsone.generator.system.netty.send.socket;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.concurrent.CompletableFuture;
//
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.SwingUtilities;
//
//import com.nsone.generator.system.constant.SystemConstMessage;
////import com.nsone.gateway.buffer.GlobalTcpUdpBuffer;
////import com.nsone.gateway.h2.jpa.TcpUdpMappingJpa;
////import com.nsone.gateway.share.util.TimeString;
////import com.nsone.gateway.ui.util.StringMessageMaker;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFuture;
//import io.netty.util.CharsetUtil;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class SendService {
//	//
//	private Bootstrap sendTcpClientBootstrap;
//	private Channel tcpClientChannel;
//	
//	public void setTcpBootstrap(Bootstrap sendTcpClientBootstrap) {
//		//
//		this.sendTcpClientBootstrap = sendTcpClientBootstrap;
//	}
//	
//	public void setTcpClientChannel(Channel tcpClientChannel) {
//		//
//		this.tcpClientChannel = tcpClientChannel;
//	}
//	
//	public boolean reConnectTcp(String ip, int port) {
//		//
//		try {
//			//
//			log.info("TCP 재연결 시작");
//			ChannelFuture serverChannelFuture = this.sendTcpClientBootstrap.connect(new InetSocketAddress(ip, port))
//					.sync();
//			this.tcpClientChannel = serverChannelFuture.channel();
//
//			if (this.tcpClientChannel.isActive()) {
//				//
//				log.info("TCP 송신 Ready OK.");
//				return true;
//			} else {
//				//
//				log.info("TCP 송신 Ready Fail.");
//				return false;
//			}
//		} catch (Exception e) {
//			//
//			log.error(e.getMessage());
//			return false;
//		}
//	}
//	
//	public void sendTcpData(String message) {
//		//
//		this.writeAndFlushTcp(message);
//	}
//	
//	private void writeAndFlushTcp(String data) {
//        //
//		this.tcpClientChannel.writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8));
//    }
//	
//}
