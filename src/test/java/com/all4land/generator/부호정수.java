//package com.all4land.generator;
//
//public class 부호정수 {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		int originalValue = 60002;
//
//		byte[] b = new byte[2];
//        b[0] = (byte) ((originalValue >> 8) & 0xFF);
//        b[1] = (byte) (originalValue & 0xFF);
//		
//        // 분할된 바이트 값 출력
//        System.out.println("b[0]: " + (b[0] & 0xFF)); // 234 (0xEA)
//        System.out.println("b[1]: " + (b[1] & 0xFF)); // 82 (0x52)
//
//        // 값을 복원
//        int restoredValue = ((b[0] & 0xFF) << 8) | (b[1] & 0xFF);
//
//        // 결과 출력
//        System.out.println("Original value: " + originalValue); // 60002
//        System.out.println("Restored value: " + restoredValue); // 60002
//        
////        // int 값을 16비트로 변환하여 short에 저장
////        short shortValue = (short) (originalValue & 0xFFFF);
////
////        // short 값을 다시 int로 복원
////        int restoredValue = shortValue & 0xFFFF;
////
////        // 결과 출력
////        System.out.println("Original value: " + originalValue); // 60002
////        System.out.println("Short value: " + shortValue); // -5534
////        System.out.println("Restored value: " + restoredValue); // 60002
//
//	}
//
//}
