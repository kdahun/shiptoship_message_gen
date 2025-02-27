//package com.all4land.generator;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//import com.google.gson.Gson;
//import com.all4land.generator.system.constant.SystemConstMessage;
//
//public class Send_Alarm_JSON {
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
//    	//
//    	JsonAlarmEntity jsonImage = new JsonAlarmEntity();
//    	jsonImage.setSrcId("SC0100");
//    	jsonImage.setDestId("SV0001");
//    	jsonImage.setTxMMSI("440123456");
//    	jsonImage.setRxMMSI("004401234");
//    	jsonImage.setInfoType("22");
//    	jsonImage.setDataType("text/json@@@@@@@@@@@");
//    	jsonImage.setDataContent("{\"message\":\"A situation has occurred where the gradient is 20 degrees or more.\", \"mmsi\": \"123456789\", \"lat\":\"34.1234546\", \"lon\": \"126.4548712\", \"bow\":\"-45\", \"stern\": \"-35\"}");
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
