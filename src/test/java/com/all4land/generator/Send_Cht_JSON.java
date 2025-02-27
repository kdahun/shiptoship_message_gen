//package com.all4land.generator;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//import com.google.gson.Gson;
//import com.all4land.generator.system.constant.SystemConstMessage;
//
//public class Send_Cht_JSON {
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
//    	JsonChtEntity jsonImage = new JsonChtEntity();
//    	jsonImage.setSrcId("SC0100");
//    	jsonImage.setDestId("SV0001");
//    	jsonImage.setTxMMSI("440123456");
//    	jsonImage.setRxMMSI("004401234");
//    	jsonImage.setInfoType("22");
//    	jsonImage.setDataType("text/plain@@@@@@@@@@");
//    	jsonImage.setDataContent("? ?");
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
//    private static void initConnection() {
//        try {
//            socket = new Socket("127.0.0.1", 12345); // Replace with your server's IP and port
//            out = new PrintWriter(socket.getOutputStream(), true);
//        } catch (IOException e) {
//            System.out.println("Error initializing connection: " + e.getMessage());
//        }
//    }
//}
