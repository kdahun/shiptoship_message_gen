//package com.all4land.generator;
//
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.nio.ByteBuffer;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.all4land.generator.system.constant.SystemConstMessage;
//
//public class UDP_RAUdp_Receiver {
//    
//	public static void main(String[] args) {
//        int port = 8099; // 서버의 포트 번호
//        byte[] buffer = new byte[2048]; // 버퍼 크기 (데이터 패킷 크기에 맞게 조정)
//
//        List<RaUdpEntity> RaUdpEntityList = new ArrayList<>();
//        
//        try {
//            // UDP 소켓 생성 및 포트 바인딩
//            DatagramSocket serverSocket = new DatagramSocket(port);
//            System.out.println("UDP 서버가 포트 " + port + "에서 대기 중입니다.");
//
//            while (true) {
//                // 데이터 수신 준비
//                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
//                
//                // 데이터 수신
//                serverSocket.receive(receivePacket);
//                
//                // 수신한 데이터 패킷의 정보를 얻음
//                InetAddress clientAddress = receivePacket.getAddress();
//                int clientPort = receivePacket.getPort();
//                int length = receivePacket.getLength();
//                byte[] data = receivePacket.getData();
//                
//                // 수신한 데이터 출력
////                System.out.println("클라이언트 [" + clientAddress.getHostAddress() + ":" + clientPort + "]로부터 데이터 수신:");
//                System.out.println("data ="+new String(data));
//
//                // 수신한 데이터 파싱
//                DatagramHeader dh = parseDatagramHeader(data);
//                BinaryFileDesc bfd = parseBinaryFileDesc(data, dh.getHeaderLength(), length - dh.getHeaderLength());
//
//
//                RaUdpEntity RaUdpEntity = new RaUdpEntity();
//                RaUdpEntity.setDatagramHeader(dh);
//                RaUdpEntity.setBinaryFileDesc(bfd);
//                RaUdpEntityList.add(RaUdpEntity);
//                
//                if(dh.getMaxSeq() == dh.getSeqNumber()) {
//                	//
//                	System.out.println("finish!!!!");
//                	StringBuffer sb = new StringBuffer();
//                	for(RaUdpEntity raUdpEntityList : RaUdpEntityList) {
//                		sb.append(raUdpEntityList.getBinaryFileDesc().getBinaryFileDataString().trim());
//                	}
//                	System.out.println(sb);
//                }
//                
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static DatagramHeader parseDatagramHeader(byte[] data) {
//        ByteBuffer buffer = ByteBuffer.wrap(data);
//
//        byte[] tokenBytes = new byte[6];
//        buffer.get(tokenBytes);
//        String token = new String(tokenBytes, StandardCharsets.UTF_8);
//
//        int headerVersion = buffer.getShort();
//        int headerLength = buffer.getShort();
//
//        byte[] srcIdBytes = new byte[6];
//        buffer.get(srcIdBytes);
//        String srcId = new String(srcIdBytes, StandardCharsets.UTF_8);
//
//        byte[] destIdBytes = new byte[6];
//        buffer.get(destIdBytes);
//        String destId = new String(destIdBytes, StandardCharsets.UTF_8);
//
//        int type = buffer.getShort();
//        int blockId = buffer.getInt();
//        int seqNumber = buffer.getInt();
//        int maxSeq = buffer.getInt();
//        int device = buffer.get();
//        int channel = buffer.get();
//
//        DatagramHeader dh = new DatagramHeader();
//        dh.setToken(token);
//        dh.setHeaderVersion(headerVersion);
//        dh.setHeaderLength(headerLength);
//        dh.setSrcId(srcId);
//        dh.setDestId(destId);
//        dh.setType(type);
//        dh.setBlockId(blockId);
//        dh.setSeqNumber(seqNumber);
//        dh.setMaxSeq(maxSeq);
//        dh.setDevice(device);
//        dh.setChannel(channel);
//
//        return dh;
//    }
//
//    private static BinaryFileDesc parseBinaryFileDesc(byte[] data, int offset, int dataLength) {
//        ByteBuffer buffer = ByteBuffer.wrap(data, offset, data.length - offset);
//
//        int descLength = buffer.getInt();
//        int fileLength = buffer.getInt();
//        int statusOfAcquisition = buffer.getShort();
//        int ackDestPort = buffer.getShort();
//        int typeLength = buffer.get();
//        byte[] dataTypeBytes = new byte[typeLength];
//        buffer.get(dataTypeBytes);
//        String dataType = new String(dataTypeBytes, StandardCharsets.UTF_8);
//        int statusLength = buffer.getShort();
//        byte[] statusAndInfoTextBytes = new byte[statusLength];
//        buffer.get(statusAndInfoTextBytes);
//        String statusAndInfoText = new String(statusAndInfoTextBytes, StandardCharsets.UTF_8);
//
//        // 남은 바이트 길이 계산
//        int remainingBytes = dataLength - (4 + 4 + 2 + 2 + 1 + typeLength + 2 + statusLength);
//        byte[] binaryFileData = new byte[remainingBytes];
//        buffer.get(binaryFileData);
//        
//        BinaryFileDesc bfd = new BinaryFileDesc();
//        bfd.setDescLength(descLength);
//        bfd.setFileLength(fileLength);
//        bfd.setStatusOfAcquisition(statusOfAcquisition);
//        bfd.setAckDestPort(ackDestPort);
//        bfd.setTypeLength(typeLength);
//        bfd.setDataType(dataType);
//        bfd.setStatusLength(statusLength);
//        bfd.setStatusAndInfoText(statusAndInfoText);
//        bfd.setBinaryFileData(binaryFileData);
//
//        return bfd;
//    }
//}
