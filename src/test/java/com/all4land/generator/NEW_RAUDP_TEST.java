package com.all4land.generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NEW_RAUDP_TEST {
	//
	private static final int LIMIT_LENGTH = 1472;
	private static int globalSeq = 0;

	private static final int DATAGRAM_HEADER_LENGTH = 38;
	private static final int BINARY_FILE_DESC_LENGTH = 35; // 35+ n

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Status and Info text
		String SAIT = "";
		
		byte[] data = new byte[2000];

	    for (int i = 0; i < 2000; i++) {
	        // 1부터 2000까지 숫자를 byte로 변환하여 배열에 저장
	        data[i] = (byte) (i + 1);
	    }

		// 결과를 담을 리스트
        List<byte[]> result = new ArrayList<>();
		
		// 데이터를 앞에서부터 소비하기 위한 인덱스
        int index = 0;
        int chunkSize = 1; // 처음에는 1바이트부터 시작
		
		// 길이계산
		if ((data.length + DATAGRAM_HEADER_LENGTH + (BINARY_FILE_DESC_LENGTH+SAIT.length())) > LIMIT_LENGTH) {
			//
			// 여러개를 보내야한다.
			while (index < data.length) {
	            // 남은 데이터 길이를 계산
	            int remaining = data.length - index;

	            // 소비할 chunkSize가 남은 데이터 길이보다 클 수 없으므로, 남은 데이터를 고려하여 처리
	            int currentChunkSize = Math.min(chunkSize, remaining);

	            // 현재 chunkSize만큼 데이터를 추출
	            byte[] chunk = new byte[currentChunkSize];
	            System.arraycopy(data, index, chunk, 0, currentChunkSize);

	            // 결과 리스트에 추가
	            result.add(chunk);

	            // 인덱스와 chunkSize 업데이트
	            index += currentChunkSize;
	            chunkSize++;
	        }
		} else {
			//
			// 하나짜리 메시지 보낸다
		}

		
		// 결과 출력 (옵션)
        for (byte[] chunk : result) {
            System.out.println("Chunk: " + byteArrayToString(chunk));
        }
		
	}

	// 바이트 배열을 문자열로 변환 (옵션)
    private static String byteArrayToString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(b).append(" ");
        }
        return sb.toString();
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

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}
}
