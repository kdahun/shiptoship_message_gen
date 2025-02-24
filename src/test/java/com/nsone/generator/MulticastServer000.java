//package com.nsone.generator;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.DatagramChannelConfig;
//import io.netty.channel.socket.DatagramPacket;
//import io.netty.channel.socket.nio.NioDatagramChannel;
//import io.netty.util.CharsetUtil;
//
//import java.net.InetSocketAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//
//public class MulticastServer000 {
//
//    public void run(int port, String multicastGroup) throws Exception {
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//             .channel(NioDatagramChannel.class)
//             .option(ChannelOption.SO_REUSEADDR, true)
////             .option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true) // Disable multicast loopback
////             .option(ChannelOption.IP_MULTICAST_IF, findNetworkInterface())
////             .option(ChannelOption.IP_MULTICAST_TTL, 255)
//             .handler(new ChannelInboundHandlerAdapter() {
//                 @Override
//                 public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                     DatagramPacket packet = (DatagramPacket) msg;
//                     String received = packet.content().toString(CharsetUtil.UTF_8);
//                     System.out.println("Received: " + received);
//                 }
//             });
//
//            NioDatagramChannel channel = (NioDatagramChannel)b.bind(new InetSocketAddress(port)).sync().channel();
//            channel.joinGroup(new InetSocketAddress(multicastGroup, port), findNetworkInterface()).sync();
//            
//         // Disable loopback so that server does not receive its own packets
//            DatagramChannelConfig config = (DatagramChannelConfig) channel.config();
//            config.setOption(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true);
//            
//            
//            // Send a message to the multicast group
//            String message = "Hello11, Multicast World!";
//            channel.writeAndFlush(new DatagramPacket(
//                Unpooled.copiedBuffer(message.getBytes()),
//                new InetSocketAddress(multicastGroup, port))).sync();
//
//            System.out.println("Message sent: " + message);
//            
//            channel.closeFuture().await();
//        } finally {
//            group.shutdownGracefully();
//            System.out.println("end");
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        int port = 50001;
//        String multicastGroup = "239.0.0.1"; // Example multicast group
//
//        new MulticastServer000().run(port, multicastGroup);
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
////            log.error(e.getMessage());
//            return networkInterface;
//        }
//
//        if (networkInterface == null) {
////            log.info("No suitable network interface found.");
//            return networkInterface;
//        }
//		return networkInterface;
//	}
//}
//
