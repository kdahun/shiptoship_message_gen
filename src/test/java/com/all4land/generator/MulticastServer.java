//package com.all4land.generator;
//
//import java.net.InetSocketAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.DatagramPacket;
//import io.netty.channel.socket.nio.NioDatagramChannel;
//import io.netty.util.CharsetUtil;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class MulticastServer {
//
//    private final static String MULTICAST_GROUP = "239.0.0.1";
//    private final static int PORT = 50001;
//
//    public static void main(String[] args) throws Exception {
//        NioEventLoopGroup group = new NioEventLoopGroup();
//        try {
//        	NetworkInterface ni = findNetworkInterface();
//        	
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//             .channel(NioDatagramChannel.class)
//             .localAddress("192.0.0.5", 50001)
//             .option(ChannelOption.SO_BROADCAST, true)
//             .option(ChannelOption.SO_REUSEADDR, true)
//             .option(ChannelOption.IP_MULTICAST_IF, ni)
//             .option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, false) // Disable multicast loopback
//             .option(ChannelOption.IP_MULTICAST_TTL, 255)
////             .handler(new ChannelInboundHandlerAdapter() {
////                 @Override
////                 public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
////                     DatagramPacket packet = (DatagramPacket) msg;
////                     String received = packet.content().toString(CharsetUtil.UTF_8);
////                     System.out.println("Received: " + received);
////                 }
////             });
//             .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
//                 @Override
//                 protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
//                     String received = packet.content().toString(CharsetUtil.UTF_8);
//                     System.out.println("Received: " + received);
//                 }
//             });
//
//            NioDatagramChannel ch = (NioDatagramChannel) b.bind(PORT).sync().channel();
//
//            ch.joinGroup(new InetSocketAddress(MULTICAST_GROUP, PORT), ni).sync();
//            
//            // 데이터 전송
//            ch.writeAndFlush(new DatagramPacket(
//            		Unpooled.wrappedBuffer("Server Send1111".getBytes()),
//                new InetSocketAddress(MULTICAST_GROUP, PORT)));
//            
//            ch.writeAndFlush(new DatagramPacket(
//            		Unpooled.wrappedBuffer("Server Send222".getBytes()),
//                new InetSocketAddress(MULTICAST_GROUP, PORT)));
//
//            ch.closeFuture().await();
//        } finally {
//            group.shutdownGracefully();
//        }
//    }
//    
//    private static NetworkInterface findNetworkInterface() {
//		//
//		// 네트워크 인터페이스 가져오기
//		NetworkInterface networkInterface = null;
//        try {
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface ni = interfaces.nextElement();
//                if (ni.isUp() && !ni.isLoopback()) {
//                    networkInterface = ni;
//                    break;
//                }
//            }
//        } catch (SocketException e) {
//            log.error(e.getMessage());
//            return networkInterface;
//        }
//
//        if (networkInterface == null) {
//            log.info("No suitable network interface found.");
//            return networkInterface;
//        }
//		return networkInterface;
//	}
//}
//
