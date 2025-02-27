//package com.all4land.generator;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.InterfaceAddress;
//import java.net.MulticastSocket;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.google.gson.Gson;
//import com.all4land.generator.system.constant.SystemConstMessage;
//import com.all4land.generator.ui.entity.SixbitEntity;
//import com.all4land.generator.ui.util.TimeString;
//
//public class Send_Cht_MultiCast {
//	//
//	private static final Map<String, SixbitEntity> sixbitEntityMap = new HashMap<>();
//	private static InetAddress serverAddress; // = InetAddress.getByName("239.0.0.1");
//	private static int serverPort = 50001; // 서버의 포트 번호
//	private static MulticastSocket multicastSocket;// = new MulticastSocket();
//    
//	private static int globalSeq = 0;
//	
//    public static void main(String[] args) throws Exception {
//    	//
//    	init();
//    	initConnection(findNetworkInterface());
//        while (true) {
//			//
//        	startProcess();
//        	Thread.sleep(1000);
//        	
//        	
//		}
//    }
//    
//    private static int incrementAndGet() {
//		//
//    	globalSeq ++;
//		if(globalSeq > 999) {
//			//
//			globalSeq = 1;
//		}
//		return globalSeq;
//	}
//    
//    private static void startProcess() {
//    	JsonChtEntity jsonImage = new JsonChtEntity();
//    	jsonImage.setSrcId("SC0100");
//    	jsonImage.setDestId("SV0001");
//    	jsonImage.setTxMMSI("440123456");
//    	jsonImage.setRxMMSI("004401234");
//    	jsonImage.setInfoType("22");
//    	jsonImage.setDataType("text/plain@@@@@@@@@@");
//    	jsonImage.setDataContent(String.valueOf(globalSeq));
////    	jsonImage.setDataContent("What is the current situation?");
//    	
//    	Gson gson = new Gson();
//    	String jsonString = gson.toJson(jsonImage);
//    	
//    	messageMake(jsonImage.getDataContent(), jsonImage.getDataType());
//    }
//    
//    private static void messageMake(String jsonString, String dataType) {
//    	//
////    	StringBuilder target_bytes = new StringBuilder();
////        for(byte b : bytes) { // 1 바이트는 8 비트
////        	//
////        	String bit = convertToBits(b, 8);
////        	target_bytes.append(bit);
////        }
////        
////        int dataCount = target_bytes.length(); // 11비트는 0부터 2047
////        //String dataCountBits = convertToBits(dataCount, 11);
//////        System.out.println("Serialized SixBit data: "+ dataCount);
//////        System.out.println(target_bytes.toString());
////        
////        // padding =============================
////        int paddingLength = target_bytes.toString().length() % 6;
////        if (paddingLength != 0) {
////            int additionalBits = 6 - paddingLength;
////            target_bytes.append("0".repeat(Math.max(0, additionalBits)));
////        }
//////        System.out.println("paddingLength: "+paddingLength);
////        
////        StringBuilder resultStr = new StringBuilder();
////        for (int i = 0; i < target_bytes.length(); i += 6) {
////            String sixBits = target_bytes.substring(i, i + 6);
////            resultStr.append(Send_Cht_MultiCast.sixbitEntityMap.get(sixBits).getCharValue());
////            
////        }
//        
//        makeMessage(jsonString, dataType);
//    }
//    
//    private static void makeMessage(String resultStr, String dataType) {
//    	//
//    	int udptd_total_length = 1472;
//        int use_data_length = 1472;
//        
//        String datagramHeader = "UdPbC ";
//        int datagramHeader_length = datagramHeader.length();
//        
//        String tmp_UdPtd = datagramHeader+"\\g:  -  -   ,s:MV0001,d:TB0001*43\\";
//        int tmp_UdPtd_length = tmp_UdPtd.length(); 
//        
//        String tmp_tdb = "!VATDB,  ,  ,   ,123456789,123456789,2,,"+" ";
//        int tmp_tdb_length = tmp_tdb.length();
//        
//        use_data_length = (use_data_length - tmp_UdPtd_length) - tmp_tdb_length - 4;
//    	//
////        System.out.println("짤라야될 길이: "+use_data_length);
//        
//        int total = resultStr.length() / use_data_length;
//        int skajwl = resultStr.length() % use_data_length;
//        if(skajwl > 0) {
//        	total = total+1;
//        }
//        
//        int index0 = 1;
//        int index1 = 1;
//        
//        if(dataType.indexOf("image") > -1) {
//        	System.out.println("이미지 데이터 : "+total+" 개 만들어짐." );
//        }else if(dataType.indexOf("text/json") > -1) {
//        	//
//        	System.out.println("알람 데이터 : "+total+" 개 만들어짐.");
//        }else if(dataType.indexOf("text/gml") > -1) {
//        	//
//        	System.out.println("문서 데이터 : "+total+" 개 만들어짐.");
//        }else if(dataType.indexOf("text/plain") > -1) {
//        	//
//        	System.out.println("채팅 데이터 : "+total+" 개 만들어짐.");
//        }
//        
//        while (resultStr.length() > 0) {
//            // Ensure chunkSize does not exceed the length of the remaining buffer
//            int endIndex = Math.min(use_data_length, resultStr.length());
//
//            // Get the substring from the beginning of the buffer to endIndex
//            String chunk = resultStr.substring(0, endIndex);
//            
//            String u = datagramHeader+"\\g:" + index0 + "-" + total + "-" + incrementAndGet() + ",s:MV0001,d:TB0001*00\\";
//            String t = "!ABCHT,123456789,123456789,"+TimeString.getNowYYYYMMddHHmmss()+","+chunk +"*00"+SystemConstMessage.CRLF;
//            
//            String gg = u+t;
////            System.out.println();
////            System.out.println("전체길이: "+gg.length());
//            System.out.println(gg);
//            DatagramPacket sendPacket = new DatagramPacket(gg.getBytes(), gg.getBytes().length, serverAddress, serverPort);
//            
//            
//            try {
//				multicastSocket.send(sendPacket);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////            
//            index0 = index0 + 1; 
//            // Delete the consumed chunk from the buffer
//            resultStr = resultStr.substring(endIndex);
//        }
//    }
//    
//    private static void initConnection(NetworkInterface ethernetInterface) {
//        try {
//        	
//        	
//        	
//        	serverAddress = InetAddress.getByName("239.0.0.1");
//        	serverPort = 50001; // 서버의 포트 번호
//        	multicastSocket = new MulticastSocket();
//        	
//        	// 소켓에 이더넷 인터페이스를 설정
//            multicastSocket.setNetworkInterface(ethernetInterface);
//        	
//        } catch (IOException e) {
//            System.out.println("Error initializing connection: " + e.getMessage());
//        }
//    }
//    
//    private static void init() {
//        int startDecimal = 0;
//
//        for (int chr = 48; chr < 120; chr++) {
//            SixbitEntity s = new SixbitEntity();
//
//            // Setting bits according to custom conditions
//            int sixbitValue;
//            if (chr < 48 || chr > 119 || (chr > 87 && chr < 96)) {
//                sixbitValue = -1; // Invalid character
//            } else if (chr < 96) {
//                sixbitValue = (chr - 48) & 0x3F;
//            } else {
//                sixbitValue = (chr - 56) & 0x3F;
//            }
//            // Handle invalid characters separately
//            String bits;
//            if (sixbitValue == -1) {
//                bits = "111111"; // or any other appropriate value for invalid characters
//            } else {
//                bits = String.format("%6s", Integer.toBinaryString(sixbitValue)).replace(' ', '0');
//            }
//            s.setBits(bits);
//
//            // Setting char value
//            char charValue = (char) chr;
//            s.setCharValue(String.valueOf(charValue));
//
//            // Setting ASCII value
//            s.setAsciiValue(String.valueOf(chr));
//
//            // Setting decimal value
//            s.setDecimalValue(String.valueOf(startDecimal));
//
//            if (!String.valueOf(chr).equals("88") && !String.valueOf(chr).equals("89")
//                    && !String.valueOf(chr).equals("90") && !String.valueOf(chr).equals("91")
//                    && !String.valueOf(chr).equals("92") && !String.valueOf(chr).equals("93")
//                    && !String.valueOf(chr).equals("94") && !String.valueOf(chr).equals("95")
//
//            ) {
//                //
//                sixbitEntityMap.put(bits, s);
//                startDecimal = startDecimal + 1;
//            }
//        }
//    }
//    
//    public static NetworkInterface findNetworkInterface() {
//        NetworkInterface networkInterface = null;
//        try {
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface ni = interfaces.nextElement();
//                System.out.println("Find Network Interface: " + ni.getName());
//                if (isValidNetworkInterface(ni)) {
//                    System.out.println("Selected Network Interface: " + ni.getName());
//                    networkInterface = ni;
//                    break;
//                }
//            }
//        } catch (SocketException e) {
//            System.err.println(e.getMessage());
//            return networkInterface;
//        }
//
//        if (networkInterface == null) {
//            System.out.println("No suitable network interface found.");
//        }
//        return networkInterface;
//    }
//
//    private static boolean isValidNetworkInterface(NetworkInterface ni) throws SocketException {
//    	//
//    	// Get the OS name
//        String osName = System.getProperty("os.name").toLowerCase();
//        
//        boolean isUp = ni.isUp();
//        boolean isLoopback = ni.isLoopback();
//        boolean isVirtual = ni.isVirtual();
//        boolean supportsMulticast = ni.supportsMulticast();
//        boolean hasInet4Address = hasValidInet4Address(ni);
//        
//        if (osName.contains("win")) {
//            // Windows-specific logic (if any)
//        	System.out.println("실행환경이 윈도우다.");
//        	if(ni.getName().contains("eth")) {
//        		return isUp && !isLoopback && !isVirtual && hasInet4Address && supportsMulticast;
//        	}else {
//        		return false;
//        	}
//        } else if (osName.contains("mac")) {
//            // MacOS-specific logic (if any)
//        	System.out.println("실행환경이 맥OS다.");
//        	if(ni.getName().contains("en")) {
//        		return isUp && !isLoopback && !isVirtual && hasInet4Address && supportsMulticast;
//        	}else {
//        		return false;
//        	}
//        } else if (osName.contains("nux") || osName.contains("nix")) {
//            // Linux-specific logic (if any)
//        	System.out.println("실행환경이 리눅스다.");
//        	if(ni.getName().contains("enp")) {
//        		return isUp && !isLoopback && !isVirtual && hasInet4Address && supportsMulticast;
//        	}else {
//        		return false;
//        	}
//        }  else if (osName.contains("linux")) {
//            // Linux-specific logic (if any)
//        	System.out.println("실행환경이 리눅스(우분투)다.");
//        	if(ni.getName().contains("enp")) {
//        		return isUp && !isLoopback && !isVirtual && hasInet4Address && supportsMulticast;
//        	}else {
//        		return false;
//        	}
//        } else {
//        	System.out.println("실행환경이 무슨 환경이냐?");
//        	return false;
//        }
//    }
//	
//	private static boolean hasValidInet4Address(NetworkInterface ni) {
//        List<InterfaceAddress> addresses = ni.getInterfaceAddresses();
//        for (InterfaceAddress addr : addresses) {
//            if (addr.getAddress() instanceof Inet4Address) {
//                return true;
//            }
//        }
//        return false;
//    }
//    
//}
