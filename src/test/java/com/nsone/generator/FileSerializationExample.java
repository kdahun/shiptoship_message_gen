//package com.nsone.generator;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import lombok.Data;
//
//public class FileSerializationExample {
//	//
//	private static final List<SixbitEntity> sixbitEntityList = new ArrayList<>();
//	private static final Map<String, SixbitEntity> sixbitEntityMap = new HashMap<>();
//	
//    public static void main(String[] args) {
//    	//
//    	init();
//    	
//        // 파일 경로
//        String filePath = "C:\\Users\\Lenovo\\Downloads\\S-124 데이터셋 샘플 데이터 보내드립니다\\새 폴더\\RTE-TEST-GMIN.s421.gml";
//        DecimalFormat decimalFormat = new DecimalFormat("#.####");
//        try {
//            // 파일 읽기 준비
//            FileInputStream fis = new FileInputStream(filePath);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
//            
//            // StringBuffer 선언
//            StringBuffer stringBuffer = new StringBuffer();
//            
//            // 파일에서 데이터 읽어오기
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // 읽어온 데이터를 StringBuffer에 추가
//                stringBuffer.append(line.trim()).append(" ");
////                stringBuffer.append("\n"); // 개행 추가 (선택적)
//            }
//            
//            // 스트림 닫기
//            reader.close();
//            
//            // StringBuffer에 담긴 데이터 출력 (시리얼라이즈된 데이터라 가정)
//            System.out.println("Serialized data: "+ stringBuffer.length());
//            System.out.println(stringBuffer.toString());
//            
//            byte[] bytes = stringBuffer.toString().getBytes(); // 문자열을 바이트 배열로 변환
//            StringBuilder target_bytes = new StringBuilder();
//            for(byte b : bytes) { // 1 바이트는 8 비트
//            	//
//            	String bit = convertToBits(b, 8);
//            	target_bytes.append(bit);
//            }
//            
//            int dataCount = target_bytes.length(); // 11비트는 0부터 2047
//            //String dataCountBits = convertToBits(dataCount, 11);
//            System.out.println("Serialized SixBit data: "+ dataCount);
//            System.out.println(target_bytes.toString());
//            
//            // padding =============================
//            int paddingLength = target_bytes.toString().length() % 6;
//            if (paddingLength != 0) {
//                int additionalBits = 6 - paddingLength;
//                target_bytes.append("0".repeat(Math.max(0, additionalBits)));
//            }
//            System.out.println("paddingLength: "+paddingLength);
//            long startTime = System.nanoTime(); // 시작 시간 기록
//            StringBuilder resultStr = new StringBuilder();
//            for (int i = 0; i < target_bytes.length(); i += 6) {
//                String sixBits = target_bytes.substring(i, i + 6);
//                sixbitEntityMap.get(sixBits);
//                resultStr.append(sixbitEntityMap.get(sixBits).getCharValue());
////                for (SixbitEntity entity : sixbitEntityList) {
////                    if (entity.getBits().equals(sixBits)) {
////                        resultStr.append(entity.getCharValue());
////                        break;
////                    }
////                }
//            }
//            
//            System.out.println("Serialized SixBit String data: "+resultStr.length());
//            System.out.println(resultStr.toString());
//            
//            int udptd_total_length = 1472;
//            int use_data_length = 1472;
//            
//            String datagramHeader = "UdPtd ";
//            int datagramHeader_length = datagramHeader.length();
//            
//            String tmp_UdPtd = datagramHeader+"\\g:  -  -   ,s:MV0001,d:TB0001*43\\";
//            int tmp_UdPtd_length = tmp_UdPtd.length(); 
//            
//            String tmp_tdb = "!VATDB,  ,  ,   ,123456789,123456789,2,,"+" ";
//            int tmp_tdb_length = tmp_tdb.length();
//            
//            //int block_seperator_length = 2; // 변하지 않는다
//            //int tagBlock_length = 30;// g:1-2-34,s:MV0001,d:TB0001*43 가변적이다
//            
//            //TDB 만들수있는길이
//            //!VATDB,1~2자리, 1~2자리, 1~3자리, 10자리, 10자리,1자리, 1~3자리,??, 1자리
//            // udptd \g:1-2-34,s:MV0001,d:TB0001*43\!VATDB,1,1,1,123456789,123456789,2,,
//            
//            //UdPtd \g:1-1-1,s:MV0001,d:TB0001*43\!VATDB,99,1,1,123456789,123456789,2,,
//            //UdPtd \g:1-1-1,s:MV0001,d:TB0001*43\!VATDB,99,2,1,123456789,123456789,2,,
//            //UdPtd \g:1-1-1,s:MV0001,d:TB0001*43\!VATDB,99,3,1,123456789,123456789,2,,
//            
//            makeMessage(resultStr, paddingLength);
//            long endTime = System.nanoTime(); // 종료 시간 기록
//	        long duration = endTime - startTime; // 실행 시간 계산
//	        double seconds = duration / 1_000_000_000.0;
//	        String formattedNumber = decimalFormat.format(seconds);
//	        System.out.println("메서드 실행 시간: "+formattedNumber+" 초");
//        } catch (FileNotFoundException e) {
//            System.err.println("File not found: " + filePath);
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + filePath);
//            e.printStackTrace();
//        }
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
////            System.out.println();
////            System.out.println("전체길이: "+gg.length());
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
//                sixbitEntityMap.put(bits, s);
//                startDecimal = startDecimal + 1;
//            }
//        }
//    }
//    
//}
////@Data
////class SixbitEntity {
////	//
////	private String bits;
////	private String charValue;
////	private String asciiValue;
////	private String decimalValue;
////}
//
