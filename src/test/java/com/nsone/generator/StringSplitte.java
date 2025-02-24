package com.nsone.generator;

public class StringSplitte {
    public static void main(String[] args) {
        // 길이 100의 예시 문자열 생성
        String input = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        
        // 자를 길이 설정
        int splitLength = 7;
        
        // 배열의 크기 계산
        int arraySize = (int) Math.ceil((double) input.length() / splitLength);
        
        // 결과 배열 생성
        String[] result = new String[arraySize];
        
        // 문자열 자르기
        for (int i = 0; i < arraySize; i++) {
            int start = i * splitLength;
            int end = Math.min(start + splitLength, input.length());
            result[i] = input.substring(start, end);
        }
        
        // 결과 출력
        for (String s : result) {
            System.out.println(s);
        }
    }
}

