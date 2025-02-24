package com.nsone.generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import io.netty.util.CharsetUtil;

public class ByteConsumerWithLimit {
    private static final int LIMIT_LENGTH = 1472;
    private static int globalSeq = 0;

    private static final int DATAGRAM_HEADER_LENGTH = 38;
    private static final int BINARY_FILE_DESC_LENGTH = 35;

    private static final String CRLF = "\r\n";
    
    public static void main(String[] args) throws Exception {
    	//
    	InetAddress serverAddress = InetAddress.getByName("239.0.0.1");
        int serverPort = 50001; // 서버의 포트 번호
    	
     // 이더넷 인터페이스를 찾기 위해 네트워크 인터페이스 열거형을 얻음
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        NetworkInterface ethernetInterface = null;

        // 모든 네트워크 인터페이스를 탐색
        while (interfaces.hasMoreElements()) {
            NetworkInterface netInterface = interfaces.nextElement();
            if (netInterface.isUp() && !netInterface.isLoopback()) {
                // 인터페이스의 이름으로 이더넷 인터페이스를 판별
                // 일반적으로 이더넷 인터페이스는 eth0, en0 등의 이름을 가짐 (플랫폼에 따라 다름)
                if (netInterface.getName().startsWith("eth") || netInterface.getName().startsWith("en")) {
                    ethernetInterface = netInterface;
                    break;
                }
            }
        }

        if (ethernetInterface == null) {
            System.out.println("이더넷 인터페이스를 찾을 수 없습니다.");
            return;
        }

        System.out.println("이더넷 인터페이스: " + ethernetInterface.getName());
		
		
		MulticastSocket multicastSocket = new MulticastSocket();
		
		// 소켓에 이더넷 인터페이스를 설정
        multicastSocket.setNetworkInterface(ethernetInterface);

		// Local address and port
		System.out.println("Local Address: " + multicastSocket.getLocalAddress());
		System.out.println("Local Port: " + multicastSocket.getLocalPort());

		// Network interface
		NetworkInterface networkInterface = multicastSocket.getNetworkInterface();
		System.out.println("Network Interface: " + networkInterface.getName());

		// Join a multicast group (example group address)
		InetAddress group = InetAddress.getByName("230.0.0.1");
		multicastSocket.joinGroup(group);
		System.out.println("Joined Multicast Group: " + group.getHostAddress());
    	
        // 예시 데이터 생성
        byte[] data = getFileData();
        String SAIT = "exampleSAIT"; // SAIT 길이

        // 데이터를 소비할 시작 인덱스
        int index = 0;

        BinaryFileDesc bfd = null;
        bfd = new BinaryFileDesc();
		bfd.setStatusAndInfoText(SAIT);
		bfd.setDataType("text/gml");
		bfd.setAckDestPort(60004);
		bfd.setStatusOfAcquisition(0);
		bfd.setFileLength(data.length);
		bfd.getBinaryFileDescBytes();
        
        // 첫 번째 루프에 적용될 헤더 길이
        int firstHeaderLength = DATAGRAM_HEADER_LENGTH + bfd.getDescLength();

        // 결과 리스트
        List<byte[]> result = new ArrayList<>();

        // 첫 번째 루프: 헤더를 포함해 LIMIT_LENGTH를 넘지 않도록 처리
        if (data.length + firstHeaderLength > LIMIT_LENGTH) {
            // 첫 번째 데이터 소비 길이 계산
            int firstChunkSize = LIMIT_LENGTH - firstHeaderLength;

            // 첫 번째 chunk 생성
            byte[] firstChunk = new byte[firstChunkSize];
            System.arraycopy(data, index, firstChunk, 0, firstChunkSize);
            result.add(firstChunk);

            // 인덱스 업데이트
            index += firstChunkSize;
        }

        // 그 이후부터는 DATAGRAM_HEADER_LENGTH만 포함하여 처리
        while (index < data.length) {
            // 남은 데이터의 길이를 확인
            int remainingDataLength = data.length - index;

            // 다음 chunk 소비 길이 계산 (헤더 포함)
            int nextChunkSize = Math.min(LIMIT_LENGTH - DATAGRAM_HEADER_LENGTH, remainingDataLength);

            // 다음 chunk 생성
            byte[] nextChunk = new byte[nextChunkSize];
            System.arraycopy(data, index, nextChunk, 0, nextChunkSize);
            result.add(nextChunk);

            // 인덱스 업데이트
            index += nextChunkSize;
        }

        // 결과 출력 (옵션)
        for (int i = 0 ; i < result.size() ; i++) {
//            System.out.println("Chunk: " + byteArrayToString(chunk));
        	if(i == 0) {
        		System.out.println("예상 총길이 : " + (result.get(i).length + firstHeaderLength));
        	}else {
        		System.out.println("예상 총길이 : " + (result.get(i).length + DATAGRAM_HEADER_LENGTH));
        	}
        }
        
        int seq = incrementAndGet();
        
        System.out.println("총 메시지 갯수 : "+result.size());
        
        for (int i = 0 ; i < result.size() ; i++) {
        	//
        	DatagramHeader dh = new DatagramHeader();
            dh.setBlockId(seq);
            dh.setSeqNumber(i+1);
            dh.setMaxSeq(result.size());
        	
            
            
        	if(i == 0) {
        		//
        		sendUDP(dh, bfd, result.get(i), serverAddress, serverPort, multicastSocket);
        	}else {
        		sendUDP(dh, null, result.get(i), serverAddress, serverPort, multicastSocket);
        	}
        }
    }

    private static void sendUDP(DatagramHeader dh, BinaryFileDesc bfd, byte[] chunk, InetAddress serverAddress, int serverPort, MulticastSocket multicastSocket) {
		//
		try {
            //
			if(bfd == null) {
				//
				byte[] dhBytes = dh.getDatagramHeaderBytes();
				byte[] sendData = new byte[dhBytes.length + chunk.length];
				int pos = 0;
	            System.arraycopy(dhBytes, 0, sendData, pos, dhBytes.length);
	            pos += dhBytes.length;
	            System.arraycopy(chunk, 0, sendData, pos, chunk.length);
//	            pos += chunk.length;
//	            System.arraycopy(CRLF.getBytes(), 0, sendData, pos, CRLF.length());
	            
	            System.out.println("그이후만들어진 실제 메시지 총 길이 : "+sendData.length);
	            //System.out.println("그이후만들어진 실제 메시지 : "+new String(sendData));
	            // UDP 패킷 생성
	            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
	            
	            
	            multicastSocket.send(sendPacket);
	            
	            // 검증: 다시 문자열로 변환 후 바이트 배열로 변환하여 원본과 비교
//	            String bb = new String(sendData, CharsetUtil.UTF_8);  // sendData -> String
//	            byte[] reConvertedBytes = bb.getBytes(CharsetUtil.UTF_8);  // String -> byte[]
//
//	            // 바이트 배열 비교
//	            boolean isEqual = Arrays.equals(sendData, reConvertedBytes);
//	            System.out.println("sendData와 reConvertedBytes가 동일한가? " + isEqual);
	            
			}else {
				//
				byte[] dhBytes = dh.getDatagramHeaderBytes();
	            byte[] bfdBytes = bfd.getBinaryFileDescBytes();
	            
	            byte[] sendData = new byte[dhBytes.length + bfdBytes.length + chunk.length];
	            int pos = 0;
	            System.arraycopy(dhBytes, 0, sendData, pos, dhBytes.length);
	            pos += dhBytes.length;
	            System.arraycopy(bfdBytes, 0, sendData, pos, bfdBytes.length);
	            pos += bfdBytes.length;
	            System.arraycopy(chunk, 0, sendData, pos, chunk.length);
//	            pos += chunk.length;
	            
//	            System.arraycopy(CRLF.getBytes(), 0, sendData, pos, CRLF.length());
	            //pos += chunk.length;
	            
	            System.out.println("첫번째만들어진 실제 메시지 총 길이 : "+sendData.length);
	            //System.out.println("첫번째만들어진 실제 메시지 : "+new String(sendData));
	            // UDP 패킷 생성
	            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
	            
	            
	            multicastSocket.send(sendPacket);
	            
	            // 검증: 다시 문자열로 변환 후 바이트 배열로 변환하여 원본과 비교
//	            String bb = new String(sendData, CharsetUtil.UTF_8);  // sendData -> String
//	            byte[] reConvertedBytes = bb.getBytes(CharsetUtil.UTF_8);  // String -> byte[]
//
//	            // 바이트 배열 비교
//	            boolean isEqual = Arrays.equals(sendData, reConvertedBytes);
//	            System.out.println("sendData와 reConvertedBytes가 동일한가? " + isEqual);
			}
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
    
    // 임의로 파일 데이터를 반환하는 메소드 (예시)
    private static byte[] getFileData() {
    	//
    	String filePath = "C:\\Users\\Lenovo\\Downloads\\S-124 데이터셋 샘플 데이터 보내드립니다\\S-124-DATASET-SAMPLE-20240711-96kb.gml";
		try {
			// 파일 읽기 준비
			FileInputStream fis = new FileInputStream(filePath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			// StringBuffer 선언
			StringBuffer stringBuffer = new StringBuffer();

			// 파일에서 데이터 읽어오기
			String line;
			while ((line = reader.readLine()) != null) {
				// 읽어온 데이터를 StringBuffer에 추가
				stringBuffer.append(line.trim()).append(" ");
			}

			// 스트림 닫기
			reader.close();

//			System.out.println(stringBuffer.toString());
			return stringBuffer.toString().getBytes(); // 문자열을 바이트 배열로 변환

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
    	
//        // 1부터 2000까지 채운 데이터 배열을 반환 (테스트용)
//        byte[] data = new byte[5000];
//        for (int i = 0; i < 5000; i++) {
//            data[i] = (byte) (i + 1);
//        }
//        return data;
    }
    
    private static int incrementAndGet() {
		//
    	globalSeq ++;
		if(globalSeq > 999) {
			//
			globalSeq = 1;
		}
		return globalSeq;
	}
}

