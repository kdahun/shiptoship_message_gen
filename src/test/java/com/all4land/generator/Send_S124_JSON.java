//package com.all4land.generator;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//import com.google.gson.Gson;
//import com.all4land.generator.system.constant.SystemConstMessage;
//
//public class Send_S124_JSON {
//	private static Socket socket;
//    private static PrintWriter out;
//    
//    public static void main(String[] args) throws Exception {
//    	//
//    	initConnection();
//        while (true) {
//			//
//        	startProcess();
//        	Thread.sleep(300);
//		}
//    }
//    
//    private static void startProcess() {
//    	JsonImageEntity jsonImage = new JsonImageEntity();
//    	jsonImage.setSrcId("SC0100");
//    	jsonImage.setDestId("SV0001");
//    	jsonImage.setTxMMSI("440123456");
//    	jsonImage.setRxMMSI("004401234");
//    	jsonImage.setInfoType("21");
//    	jsonImage.setDataType("text/gml@@@@@@@@@@@@");
//    	jsonImage.setDataContent(getFileData());
//    	
//    	Gson gson = new Gson();
//    	String jsonString = gson.toJson(jsonImage);
//    	
//    	try {
//            if (out != null) {
//                out.println("<m-iot>"+jsonString+"</m-iot>");
//                System.out.println("JSON data sent over TCP: " + jsonString);
//            } else {
//                System.out.println("Output stream is not initialized.");
//            }
//        } catch (Exception e) {
//            System.out.println("Error sending JSON data over TCP: " + e.getMessage());
//        }
//    }
//    
//    private static String getFileData() {
//		//
//		String filePath = "C:\\Users\\Lenovo\\Downloads\\S-124 데이터셋 샘플 데이터 보내드립니다\\S-124-DATASET-SAMPLE-20240711-96kb.gml";
//        try {
//            // 파일 읽기 준비
//            FileInputStream fis = new FileInputStream(filePath);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
//            
//            // StringBuffer 선언
//            StringBuffer stringBuffer = new StringBuffer();
//            
//            int cnt = 0;
//            
//            // 파일에서 데이터 읽어오기
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // 읽어온 데이터를 StringBuffer에 추가
//                if(cnt >= 1714 ) {
////                	System.out.println("!!!");
//                }
//            	stringBuffer.append(line.trim()).append(" ");
//                cnt += 1;
//            }
//            
//            // 스트림 닫기
//            reader.close();
//            
//            System.out.println(stringBuffer.toString());
//            return stringBuffer.toString();
//            
//        }catch (Exception e) {
//			// TODO: handle exception
//        	e.printStackTrace();
//        	return null;
//		}
//		
//	}
//    
//    private static void initConnection() {
//        try {
//            socket = new Socket("127.0.0.1", 12345); // Replace with your server's IP and port
//            out = new PrintWriter(socket.getOutputStream(), true);
//        } catch (IOException e) {
//            System.out.println("Error initializing connection: " + e.getMessage());
//        }
//    }
//}
