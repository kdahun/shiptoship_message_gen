//package com.all4land.generator;
//
//
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.imageio.ImageIO;
//import javax.imageio.ImageWriteParam;
//import javax.imageio.ImageWriter;
//import javax.imageio.stream.MemoryCacheImageOutputStream;
//
//import com.google.gson.Gson;
//import com.all4land.generator.system.constant.SystemConstMessage;
//
//import lombok.Data;
//
//public class Send_Image_JSON {
//	//
//	private static final List<SixbitEntity> sixbitEntityList = new ArrayList<>();
//	private static Socket socket;
//    private static PrintWriter out;
//	
//    public static void main(String[] args) throws Exception {
//    	//
//    	init();
//    	initConnection();
//        while (true) {
//			//
//        	startProcess();
//        	Thread.sleep(300);
//		}
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
//    
//    private static void startProcess() {
//    	//
//    	String inputImagePath = "C:\\image\\original.jpg";
//        String outputImagePath = "C:\\image\\original_comp_size20kb.jpg";
//        
//        long targetFileSize = 20 * 1024; // 33kB
//        float quality = 0.5f;
//        
//        int scaledWidth = 650;//800;
//        int scaledHeight = 433;//600;
//        
//        
//        
//        try {
//            // 원본 이미지 읽기
//            BufferedImage originalImage = ImageIO.read(new File(inputImagePath));
////            File outputFile = new File(outputImagePath);
//            
//            //===============================================================================================
//            // 새로운 크기의 이미지 생성
//            BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, originalImage.getType());
//            
//            // 그래픽 객체 가져오기
//            Graphics2D g2d = resizedImage.createGraphics();
//            // 리사이즈 과정에서 고품질을 위한 설정
//            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//            g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
//            g2d.dispose();
//
//            // 리사이즈된 이미지 저장
//            //ImageIO.write(resizedImage, "jpg", new File(outputImagePath));
//            
//            //=========================================================================================
//            // ImageWriter를 통해 이미지 쓰기 설정
//            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
//            if (!writers.hasNext()) {
//                throw new IllegalStateException("No writers found");
//            }
//            ImageWriter writer = writers.next();
//
//            // 파일 크기를 만족할 때까지 압축 품질 조정
//            while (true) {
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                MemoryCacheImageOutputStream mcios = new MemoryCacheImageOutputStream(baos);
//                writer.setOutput(mcios);
//                
//                // 압축 설정
//                ImageWriteParam param = writer.getDefaultWriteParam();
//                if (param.canWriteCompressed()) {
//                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//                    param.setCompressionQuality(quality);
//                }
//
//                // 이미지 쓰기
//                writer.write(null, new javax.imageio.IIOImage(resizedImage, null, null), param);
//                
//                // 리소스 해제
//                mcios.close();
//
//                // 파일 크기 확인
//                long fileSize = baos.size();
//                if (fileSize <= targetFileSize || quality <= 0.0f) {
//                    // 압축된 이미지를 파일로 저장
//                    try (FileOutputStream fos = new FileOutputStream(outputImagePath)) {
//                        fos.write(baos.toByteArray());
//                    }
//                    
//                	String tmpString = new String(baos.toByteArray());
//                	System.out.println("순수 이미지 파일 용량: "+tmpString.length());
//                	
//                	byte[] byteArray = baos.toByteArray();
//                    String base64String = Base64.getEncoder().encodeToString(byteArray);
//                    System.out.println("base64 data:"+base64String);
//                    System.out.println("base64 length: "+base64String.length());
////                    
//                    System.out.println("==================================================================");
//                    System.out.println("==================================================================");
//                    System.out.println("==================================================================");
//                    System.out.println("Final Compression Quality: " + quality);
//                    System.out.println("Final File Size: " + fileSize / 1024 + "kB");
////                    messageMake(base64String.getBytes());
//                    
//                    sendTCPjson(base64String);
//                    
//                    break;
//                }
//                
//                // 압축 품질 감소
//                quality -= 0.01f;
//                if (quality < 0.0f) {
//                    quality = 0.0f;
//                }
////                System.out.println(quality);
//            }
//
//            writer.dispose();
//            System.out.println("Image compressed successfully.");
//            
//        } catch (IOException e) {
//            System.out.println("Error compressing the image: " + e.getMessage());
//        }
//    }
//    
//    private static void sendTCPjson(String base64String) {
//    	//
//    	JsonImageEntity jsonImage = new JsonImageEntity();
//    	jsonImage.setSrcId("SC0100");
//    	jsonImage.setDestId("SV0001");
//    	jsonImage.setTxMMSI("440123456");
//    	jsonImage.setRxMMSI("004401234");
//    	jsonImage.setInfoType("22");
//    	jsonImage.setDataType("image/jpg@@@@@@@@@@@");
//    	jsonImage.setDataContent(base64String);
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
//    	
////		try (Socket socket = new Socket("127.0.0.1", 12345); // Replace with your server's IP and port
////				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
////
////			out.println("@@@@@"+jsonString+SystemConstMessage.CRLF);
////			System.out.println("JSON data sent over TCP: " + jsonString);
////
////		} catch (IOException e) {
////			System.out.println("Error sending JSON data over TCP: " + e.getMessage());
////		}
//    	
//    }
//    
//    private static void messageMake(byte[] bytes) {
//    	//
//    	StringBuilder target_bytes = new StringBuilder();
//        for(byte b : bytes) { // 1 바이트는 8 비트
//        	//
//        	String bit = convertToBits(b, 8);
//        	target_bytes.append(bit);
//        }
//        
//        int dataCount = target_bytes.length(); // 11비트는 0부터 2047
//        //String dataCountBits = convertToBits(dataCount, 11);
//        System.out.println("Serialized SixBit data: "+ dataCount);
//        System.out.println(target_bytes.toString());
//        
//        // padding =============================
//        int paddingLength = target_bytes.toString().length() % 6;
//        if (paddingLength != 0) {
//            int additionalBits = 6 - paddingLength;
//            target_bytes.append("0".repeat(Math.max(0, additionalBits)));
//        }
//        System.out.println("paddingLength: "+paddingLength);
//        
//        StringBuilder resultStr = new StringBuilder();
//        for (int i = 0; i < target_bytes.length(); i += 6) {
//            String sixBits = target_bytes.substring(i, i + 6);
//            for (SixbitEntity entity : sixbitEntityList) {
//                if (entity.getBits().equals(sixBits)) {
//                    resultStr.append(entity.getCharValue());
//                    break;
//                }
//            }
//        }
//        
//        System.out.println("Serialized SixBit String data: "+resultStr.length());
//        System.out.println(resultStr.toString());
//        
//        int udptd_total_length = 1472;
//        int use_data_length = 1472;
//        
//        String datagramHeader = "UdPtd ";
//        int datagramHeader_length = datagramHeader.length();
//        
//        String tmp_UdPtd = datagramHeader+"\\g:  -  -   ,s:MV0001,d:TB0001*43\\";
//        int tmp_UdPtd_length = tmp_UdPtd.length(); 
//        
//        String tmp_tdb = "!VATDB,  ,  ,   ,123456789,123456789,2,,"+" ";
//        int tmp_tdb_length = tmp_tdb.length();
//        
//        makeMessage(resultStr, paddingLength);
//    }
//    
//    private static void makeMessage(StringBuilder resultStr, int paddingLength) {
//    	//
//    	int udptd_total_length = 1472;
//        int use_data_length = 1472;
//        
//        String datagramHeader = "UdPtd ";
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
//        System.out.println("짤라야될 길이: "+use_data_length);
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
//        while (resultStr.length() > 0) {
//            // Ensure chunkSize does not exceed the length of the remaining buffer
//            int endIndex = Math.min(use_data_length, resultStr.length());
//
//            // Get the substring from the beginning of the buffer to endIndex
//            String chunk = resultStr.substring(0, endIndex);
//            
//            String u = datagramHeader+"\\g:" + index0 + "-" + total + "-" + index1 + ",s:MV0001,d:TB0001*00\\";
//            String t = "!VATDB,"+total+ ","+ index0 +","+index1+",123456789,123456789,2,,"+chunk+","+paddingLength+"*00";
//            
//            String gg = u+t;
//            System.out.println();
//            System.out.println("전체길이: "+gg.length());
//            System.out.println(gg);
//            index0 = index0 + 1; 
//            // Delete the consumed chunk from the buffer
//            resultStr.delete(0, endIndex);
//            
//        }
//    }
//    
//    private static String convertToBits(long value, int length) {
//    	//
//        String bits = Long.toBinaryString(value); // 입력된 값의 이진수 문자열로 변환
//        // 남은 비트를 0으로 채워주기 위해 부족한 비트만큼 0을 앞에 추가
//        while (bits.length() < length) {
//            bits = "0" + bits;
//        }
//        return bits;
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
//                sixbitEntityList.add(s);
//                startDecimal = startDecimal + 1;
//            }
//        }
//    }
//}
//@Data
//class SixbitEntity {
//	//
//	private String bits;
//	private String charValue;
//	private String asciiValue;
//	private String decimalValue;
//}
