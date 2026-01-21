package com.all4land.generator.system.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.all4land.generator.ui.entity.SixbitEntity;

@Service
public class SixbitEntityGenerator {
    private static final List<SixbitEntity> sixbitEntityList = new ArrayList<>();
    private static final Map<String, SixbitEntity> sixbitEntityMap = new HashMap<>();

    private SixbitEntityGenerator() {
        init();
    }

    public static List<SixbitEntity> getSixbitEntityList() {
        return sixbitEntityList;
    }

    private void init() {
        int startDecimal = 0;

        for (int chr = 48; chr < 120; chr++) {
            SixbitEntity s = new SixbitEntity();

            // Setting bits according to custom conditions
            int sixbitValue;
            if (chr < 48 || chr > 119 || (chr > 87 && chr < 96)) {
                sixbitValue = -1; // Invalid character
            } else if (chr < 96) {
                sixbitValue = (chr - 48) & 0x3F;
            } else {
                sixbitValue = (chr - 56) & 0x3F;
            }
            // Handle invalid characters separately
            String bits;
            if (sixbitValue == -1) {
                bits = "111111"; // or any other appropriate value for invalid characters
            } else {
                bits = String.format("%6s", Integer.toBinaryString(sixbitValue)).replace(' ', '0');
            }
            s.setBits(bits);

            // Setting char value
            char charValue = (char) chr;
            s.setCharValue(String.valueOf(charValue));

            // Setting ASCII value
            s.setAsciiValue(String.valueOf(chr));

            // Setting decimal value
            s.setDecimalValue(String.valueOf(startDecimal));

            if (!String.valueOf(chr).equals("88") && !String.valueOf(chr).equals("89")
                    && !String.valueOf(chr).equals("90") && !String.valueOf(chr).equals("91")
                    && !String.valueOf(chr).equals("92") && !String.valueOf(chr).equals("93")
                    && !String.valueOf(chr).equals("94") && !String.valueOf(chr).equals("95")

            ) {
                //
                sixbitEntityList.add(s);
                sixbitEntityMap.put(bits, s);
                startDecimal = startDecimal + 1;
            }
        }
    }
    
    public List<String> makeSixbitMessage(long mmsi, String binaryData, int slotCount) {
    	//
    	long messageid = 15; // 변경하고자 하는 값
        String messageidbits = convertToBits(messageid, 4); // 값을 4비트로 변경
        
        long retransmitFlag = 0; // 변경하고자 하는 값
        String retransmitFlagBits = convertToBits(retransmitFlag, 1);
        
        long repeatIndcator = 3; // 변경하고자 하는 값
        String repeatIndcatorBits = convertToBits(repeatIndcator, 2);
        
        long sessionId = 63; // 변경하고자 하는 값 0~ 63
        String sessionIdBits = convertToBits(sessionId, 6);
        
        long sourceId = mmsi; // 변경하고자 하는 값 0~ 4294967295L
        String sourceIdBits = convertToBits(sourceId, 32);
        
        byte[] bytes = binaryData.getBytes(); // 문자열을 바이트 배열로 변환
        StringBuilder binary = new StringBuilder();
        for(byte b : bytes) { // 1 바이트는 8 비트
        	//
        	String bit = convertToBits(b, 8);
        	binary.append(bit);
        }
        
        long dataCount = binary.length(); // 11비트는 0부터 2047
        String dataCountBits = convertToBits(dataCount, 11);
        
        //채우자
        // if(slotCount == 1) {
        // 	//
        // 	while (binary.length() < 280) {
        //     	binary.append("0");
        //     }
        // }else if(slotCount == 2) {
        // 	while (binary.length() < 792) {
        //     	binary.append("0");
        //     }
        // }else if(slotCount == 3) {
        // 	while (binary.length() < 1304) {
        //     	binary.append("0");
        //     }
        // }
        
        int dac = 999; // DAC 0~999 까지 1000 이상은 예약되어있다
        int function = 63;// 6bit // 변경하고자 하는 값 0~ 63
        String dacBits = convertToBits(dac, 10);
        String functionBits = convertToBits(function, 6);
        
        StringBuilder sb = new StringBuilder();
        sb.append(messageidbits); //MESSAGE ID
        sb.append(retransmitFlagBits); //RETRANSMIT FLAG
        sb.append(repeatIndcatorBits); //REPEAT INDICATOR
        sb.append(sessionIdBits); //SESSION ID
        sb.append(sourceIdBits); //SOURCE ID
        sb.append(dataCountBits); // dataCountBits
        sb.append(dacBits+functionBits); // ASM identifier
        sb.append(binary); // binary data
        
        int paddingLength = sb.toString().length() % 6;
        if (paddingLength != 0) {
            int additionalBits = 6 - paddingLength;
            sb.append("0".repeat(Math.max(0, additionalBits)));
        }
        
        String bitString = sb.toString();

        StringBuilder resultStr = new StringBuilder();
        for (int i = 0; i < bitString.length(); i += 6) {
            String sixBits = bitString.substring(i, i + 6);
            sixbitEntityMap.get(sixBits);
            resultStr.append(sixbitEntityMap.get(sixBits).getCharValue());
//            for (SixbitEntity entity : sixbitEntityList) {
//                if (entity.getBits().equals(sixBits)) {
//                    resultStr.append(entity.getCharValue());
//                    break;
//                }
//            }
        }

        // Printing the generated string
//        System.out.println("Generated string: " + resultStr.toString());
        List<String> returnList = new ArrayList<>();
        if(resultStr.toString().length() > 63) {
        	//
        	for (int i = 0; i < resultStr.length(); i += 63) {
        	    int endIndex = Math.min(i + 63, resultStr.length());
        	    returnList.add(resultStr.substring(i, endIndex));
        	}
        }else {
        	returnList.add(resultStr.toString());
        }
        
        return returnList;
    }
    
    public SixbitEntity findEntityForChar(List<SixbitEntity> entityList, char searchChar) {
	    for (SixbitEntity entity : entityList) {
	        if (entity.getCharValue().charAt(0) == searchChar) {
	            return entity;
	        }
	    }
	    return null;
	}
	
	public String convertToBits(long value, int length) {
    	//
        String bits = Long.toBinaryString(value); // 입력된 값의 이진수 문자열로 변환
        // 남은 비트를 0으로 채워주기 위해 부족한 비트만큼 0을 앞에 추가
        while (bits.length() < length) {
            bits = "0" + bits;
        }
        return bits;
    }
	
	public long convertToValueLong(String bits, int length) {
        return Long.parseLong(bits, 2);
    }
}