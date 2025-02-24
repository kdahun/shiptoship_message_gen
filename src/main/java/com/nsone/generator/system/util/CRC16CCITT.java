package com.nsone.generator.system.util;

import java.util.Locale;

public class CRC16CCITT {
	//
	private static final int POLYNOMIAL = 0x1021;

    // CRC 테이블
    private static final int[] CRC_TABLE = generateCRCTable();

    // CRC 테이블 생성
    private static int[] generateCRCTable() {
        int[] crcTable = new int[256];
        for (int i = 0; i < 256; i++) {
            int crc = 0;
            int c = i << 8;
            for (int j = 0; j < 8; j++) {
                if (((crc ^ c) & 0x8000) != 0) {
                    crc = (crc << 1) ^ POLYNOMIAL;
                } else {
                    crc = crc << 1;
                }
                c = c << 1;
            }
            crcTable[i] = crc & 0xFFFF;
        }
        return crcTable;
    }

    // CRC-16-CCITT 계산
    public static String calculateCRC16(String input) {
        byte[] bytes = input.getBytes();
        int crc = 0xFFFF;
        for (byte b : bytes) {
            crc = (crc << 8) ^ CRC_TABLE[((crc >> 8) ^ (b & 0xFF)) & 0xFF];
        }
        crc &= 0xFFFF; // Ensure it's within 16 bits
        return String.format(Locale.US, "%04X", crc).substring(2); // Format as 4-character hex string
    }
}
