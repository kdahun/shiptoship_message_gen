package com.nsone.generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class R000 {
	//
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] fileData = getFileData();
		String SAIT = "exampleSAIT";
		// 결과 리스트
        List<byte[]> result = new ArrayList<>();
        
        List<byte[]> result2 = new ArrayList<>();
		
        DatagramHeader dh = new DatagramHeader();
        dh.setBlockId(0);
        dh.setSeqNumber(0);
        dh.setMaxSeq(0);
        byte[] dh_bytes = dh.getDatagramHeaderBytes();
        
        BinaryFileDesc bfd = new BinaryFileDesc();
        bfd.setStatusAndInfoText(SAIT);
		bfd.setDataType("text/gml");
		bfd.setAckDestPort(60004);
		bfd.setStatusOfAcquisition(0);
		bfd.setFileLength(fileData.length);
        
		byte[] bfd_bytes = bfd.getBinaryFileDescBytes();
        
		int fileDataIndex = 0;
		
		if(fileData.length > 0) {
			//
	        byte[] firstChunk = new byte[1472];
	        
	        System.arraycopy(dh_bytes, 0, firstChunk, 0, dh_bytes.length);
	        
	        System.arraycopy(bfd_bytes, 0, firstChunk, dh_bytes.length, bfd_bytes.length);
	        
	        System.arraycopy(fileData, fileDataIndex, firstChunk, dh_bytes.length + bfd_bytes.length, 1472 - (dh_bytes.length + bfd_bytes.length));
            result.add(firstChunk);
            fileDataIndex += dh_bytes.length + bfd_bytes.length;
		}
		
		if(fileDataIndex >= fileData.length) {
			for(int i = 0; i < result.size() ; i++) {
				//
				byte[] firstChunk2 = new byte[dh_bytes.length + bfd_bytes.length + fileData.length];
				
			}
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
}
