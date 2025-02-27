package com.all4land.generator;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class RAUDP_450_test {
	//
	private static final int Limit = 1472;
	private static final int header = 38;
	private static int globalSeq = 0;
	
	private static final int DATAGRAM_HEADER_LENGTH = 38;
	private static final int BINARY_FILE_DESC_LENGTH = 35; // 35+ n
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		byte[] data = getFileData();
		
		// 길이계산
		if((data.length + DATAGRAM_HEADER_LENGTH + BINARY_FILE_DESC_LENGTH) > Limit ) {
			//
			// 여러개를 보내야한다.
		}else {
			//
			// 하나짜리 메시지 보낸다
		}
		
		
		int chunkSize = 1024; // 예: 1024 바이트(1KB) 크기의 청크로 나누기

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
		
//		BinaryFileDesc bfd = new BinaryFileDesc();
//		bfd.setStatusAndInfoText("success");
//		bfd.setDataType("text/gml");
//		bfd.setAckDestPort(60002);
//		bfd.setStatusOfAcquisition(0);
//		bfd.setFileLength((Limit - 38) - (4 + 4+ 2 + 2 + 1  + 8 + 2 + 7));
//		bfd.setDescLength(4 + 4+ 2 + 2 + 1  + 8 + 2 + 7);
//		
		chunkSize = Limit - (38 + 4 + 4+ 2 + 2 + 1  + 8 + 2 + 7) ;
			
		int totalBlock = data.length / chunkSize;
		int skajwl = data.length % chunkSize;
		if(skajwl > 0) {
			totalBlock = totalBlock + 1;
        }
		
		while (true) {
			//
			consumeDataInChunks(data, chunkSize, totalBlock, multicastSocket);
			System.out.println("데이터 전송 완료");
			Thread.sleep(1000);
		}
		
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
	
	// 바이트 배열을 특정 크기로 나누어 소비하는 메소드
    public static void consumeDataInChunks(byte[] data, int chunkSize, int totalBlock, MulticastSocket multicastSocket) throws Exception {
        int dataLength = data.length;
        int offset = 0;

        int blockId = 1;
        
        InetAddress serverAddress = InetAddress.getByName("239.0.0.1");
        int serverPort = 50001; // 서버의 포트 번호
        
        int seq = incrementAndGet();
        
        while (offset < dataLength) {
            int end = Math.min(offset + chunkSize, dataLength);
            byte[] chunk = new byte[end - offset];
            System.arraycopy(data, offset, chunk, 0, chunk.length);
            
            // 각 청크를 처리하는 부분
            processChunk(chunk, seq ,blockId, totalBlock, serverAddress, serverPort, multicastSocket);
            blockId += 1;
            offset = end;
        }
    }
    
    // 각 청크를 처리하는 메소드 (예시)
    public static void processChunk(byte[] chunk, int seq, int blockId, int totalBlock, InetAddress serverAddress, int serverPort, MulticastSocket multicastSocket) {
        // 청크 처리 로직을 여기에 작성
        DatagramHeader dh = new DatagramHeader();
        dh.setBlockId(seq);
        dh.setSeqNumber(blockId);
        dh.setMaxSeq(totalBlock);
        
		BinaryFileDesc bfd = new BinaryFileDesc();
		bfd.setStatusAndInfoText("success");
		bfd.setDataType("text/gml");
		bfd.setAckDestPort(60002);
		bfd.setStatusOfAcquisition(0);
		bfd.setFileLength(chunk.length);
		bfd.setDescLength(4 + 4+ 2 + 2 + 1  + 8 + 2 + 7);
		
		StringBuffer sb = new StringBuffer();
		sb.append(dh.getToken()).append(" ");
		sb.append(dh.getHeaderVersion()).append(" ");
		sb.append(dh.getHeaderLength()).append(" ");
		sb.append(dh.getSrcId()).append(" ");
		sb.append(dh.getDestId()).append(" ");
		sb.append(dh.getType()).append(" ");
		sb.append(dh.getBlockId()).append(" ");
		sb.append(dh.getSeqNumber()).append(" ");
		sb.append(dh.getMaxSeq()).append(" ");
		sb.append(dh.getDevice()).append(" ");
		sb.append(dh.getChannel()).append("----");
		
		sb.append(bfd.getDescLength()).append(" ");
		sb.append(bfd.getFileLength()).append(" ");
		sb.append(bfd.getStatusOfAcquisition()).append(" ");
		sb.append(bfd.getAckDestPort()).append(" ");
		sb.append(bfd.getTypeLength()).append(" ");
		sb.append(bfd.getDataType()).append(" ");
		sb.append(bfd.getStatusLength()).append(" ");
		sb.append(bfd.getStatusAndInfoText()).append("----");
		
		sb.append(chunk);
		
		sendUDP(dh, bfd, chunk, serverAddress, serverPort, multicastSocket);
//		System.out.println(sb);
    }
	

	private static void sendUDP(DatagramHeader dh, BinaryFileDesc bfd, byte[] chunk, InetAddress serverAddress, int serverPort, MulticastSocket multicastSocket) {
		//
		try {
            
            
            byte[] dhBytes = dh.getDatagramHeaderBytes();
            byte[] bfdBytes = bfd.getBinaryFileDescBytes();
            
            byte[] sendData = new byte[dhBytes.length + bfdBytes.length + chunk.length ];
            int pos = 0;
            System.arraycopy(dhBytes, 0, sendData, pos, dhBytes.length);
            pos += dhBytes.length;
            System.arraycopy(bfdBytes, 0, sendData, pos, bfdBytes.length);
            pos += bfdBytes.length;
            System.arraycopy(chunk, 0, sendData, pos, chunk.length);
            pos += chunk.length;
            
//            String a = "*00"+SystemConstMessage.CRLF;
//            
//            System.arraycopy(a.getBytes(CharsetUtil.UTF_8), 0, sendData, pos, a.length());
            
            // 2. Arrays.toString()을 사용하여 출력
//            System.out.println(java.util.Arrays.toString(sendData));
            
         // 3. String 생성자를 사용하여 출력 (바이트 배열이 문자열 데이터를 나타낼 경우)
//            String str = new String(sendData);
//            System.out.println(str);
            
//            System.out.println(sendData.length);
            // UDP 패킷 생성
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            
            
            multicastSocket.send(sendPacket);
            // UDP 소켓 생성 및 데이터 전송
//            DatagramSocket clientSocket = new DatagramSocket();
//            clientSocket.send(sendPacket);
//            
//            // 소켓 닫기
//            clientSocket.close();
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
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
            
            System.out.println(stringBuffer.toString());
            return stringBuffer.toString().getBytes(); // 문자열을 바이트 배열로 변환
            
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
        	return null;
		}
		
	}
}
