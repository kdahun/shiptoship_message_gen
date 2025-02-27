//package com.all4land.generator;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.DatagramPacket;
//import io.netty.channel.socket.nio.NioDatagramChannel;
//import io.netty.util.CharsetUtil;
//
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//
//public class MulticastServer001 {
//
//    public static void main(String[] args) throws Exception {
//        EventLoopGroup group = new NioEventLoopGroup();
//
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//             .channel(NioDatagramChannel.class)
//             .option(ChannelOption.SO_REUSEADDR, true)
//             .handler(new ChannelInitializer<NioDatagramChannel>() {
//                 @Override
//                 protected void initChannel(NioDatagramChannel ch) throws Exception {
//                     ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {
//                         @Override
//                         protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
//                             // 클라이언트로부터 데이터 수신 처리
//                        	 DatagramPacket packet = (DatagramPacket) msg;
//                             String received = packet.content().toString(CharsetUtil.UTF_8);
//                             System.out.println("Received: " + received);
//                         }
//                     });
//                 }
//             });
//
//            // 멀티캐스트 그룹 설정
//            InetAddress groupAddress = InetAddress.getByName("230.0.0.1");
//            NetworkInterface networkInterface = findNetworkInterface();//NetworkInterface.getByName("eth0");
//
//            // IP_MULTICAST_LOOP_DISABLED 옵션 활성화 (루프백 비활성화)
//            b.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true);
//
//            // 서버 바인딩
//            Channel channel = b.bind(0).sync().channel(); // 임의의 포트 바인딩
////            Channel channel = b.bind(new InetSocketAddress(50001)).sync().channel();
////            NioDatagramChannel channel = (NioDatagramChannel)b.bind(new InetSocketAddress(50001)).sync().channel();
//
//            // 멀티캐스트 그룹으로 데이터 보내기
//            String message = "Hello, Multicast World!";
////            channel.writeAndFlush(new DatagramPacket(
////                    Unpooled.copiedBuffer(message.getBytes()),
////                    new InetSocketAddress(groupAddress, 50001),
////                    networkInterface
////            )).sync();
//            DatagramPacket packet = new DatagramPacket(
//                    Unpooled.copiedBuffer(message.getBytes()),
//                    new InetSocketAddress(groupAddress, 50001)
//            );
//
//         // DatagramPacket을 보내기 전에 네트워크 인터페이스를 설정
////            channel.bind(new InetSocketAddress(50001), new DefaultChannelPromise(channel))
////                   .sync()
////                   .channel()
////                   .writeAndFlush(packet)
////                   .sync();
//            
//            channel.writeAndFlush(packet).sync();
//            
//            // 서버 종료 대기
//            channel.closeFuture().await();
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
