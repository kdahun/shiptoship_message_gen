package com.all4land.generator.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import com.all4land.generator.ui.entity.TargetCellInfoEntity;

public class RandomGenerator {
	//
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i = 0; i < 10; i++) {
			System.out.println(generateRandomInt(8));
		}
		
	}
	
	public static String generateRandomString(char startUpper, char endUpper, char startLower, char endLower, char startDigit, char endDigit, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char randomChar;

            // 랜덤하게 대문자, 소문자, 숫자 중 하나를 선택
            int choice = random.nextInt(3);
            switch (choice) {
                case 0:
                    randomChar = (char) (startUpper + random.nextInt(endUpper - startUpper + 1));
                    break;
                case 1:
                    randomChar = (char) (startLower + random.nextInt(endLower - startLower + 1));
                    break;
                case 2:
                    randomChar = (char) (startDigit + random.nextInt(endDigit - startDigit + 1));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }

            sb.append(randomChar);
        }

        return sb.toString();
    }
	
	public static String generateRandomString(char startChar, char endChar, int length) {
        if (startChar > endChar) {
            throw new IllegalArgumentException("Start character must be less than or equal to end character");
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char randomChar;
            do {
                randomChar = (char) (startChar + random.nextInt(endChar - startChar + 1));
            } while (!Character.isLetterOrDigit(randomChar));

            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static double generateRandomDouble(double start, double end, int decimalPlaces) {
        
        Random random = new Random();
        double range = end - start;
        double randomValue = start + (range * random.nextDouble());

        // 소수점 이하 자리수를 조절
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(randomValue * multiplier) / multiplier;
    }
    
    public static long generateRandomLong(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Invalid argument: length must be greater than 0");
        }

        Random random = new Random();
        
        // 최소값은 10^(length-1)이고, 최대값은 (10^length - 1)
        long minValue = (long) Math.pow(10, length - 1);
        long maxValue = (long) Math.pow(10, length) - 1;

        // 랜덤한 값을 생성하고, 최소값과 최대값으로 조절
        long randomValue = minValue + (long) ((maxValue - minValue + 1) * random.nextDouble());
        return randomValue;
    }
    
    public static int generateRandomInt(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Invalid argument: length must be greater than 0");
        }

        Random random = new Random();
        
        // 최소값은 10^(length-1)이고, 최대값은 (10^length - 1)
        int minValue = (int) Math.pow(10, length - 1);
        int maxValue = (int) Math.pow(10, length) - 1;

        // 랜덤한 값을 생성하고, 최소값과 최대값으로 조절
        return minValue + random.nextInt(maxValue - minValue + 1);
    }
    
    public static int generateRandomInt() {
        //
//    	int[] options = {180};
    	int[] options = {2, 6, 10, 180};
        Random random = new Random();
        
        int randomIndex = random.nextInt(options.length);

        return options[randomIndex];
    }
    
    public static char generateRandomChannel() {
        //
    	char[] options = {'A', 'B'};
        Random random = new Random();
        
        int randomIndex = random.nextInt(options.length);

        return options[randomIndex];
    }
    
    public static int generateRandomIntFromTo(int start, int end) {
        //
        Random random = new Random();
        // 랜덤한 값을 생성하고, 최소값과 최대값으로 조절
        return random.nextInt(start, end+1);
    }
    
    public static TargetCellInfoEntity generateRandomAisTarget(List<TargetCellInfoEntity> targetInfoList) {
        //
        Random random = new Random();
        int randomIndex = random.nextInt(targetInfoList.size());
        return targetInfoList.get(randomIndex);
    }
    
    public static LocalDateTime generateRandomLocalDateTime(int second, int minute) {
        // 현재 시간
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 랜덤한 초(0~59)와 분(0~59)을 생성
        Random random = new Random();
        int randomSeconds = random.nextInt(second);
        int randomMinutes = random.nextInt(minute);

        // 현재 시간에 랜덤한 초와 분을 더하여 새로운 LocalDateTime 생성
        LocalDateTime randomDateTime = currentDateTime.plus(randomSeconds, ChronoUnit.SECONDS)
                                                      .plus(randomMinutes, ChronoUnit.MINUTES);

        return randomDateTime;
    }
    
    public static LocalDateTime generateRandomLocalDateTime(int second) {
        // 현재 시간
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 랜덤한 초(0~59)와 분(0~59)을 생성
        Random random = new Random();
        int randomSeconds = random.nextInt(second);

        // 현재 시간에 랜덤한 초와 분을 더하여 새로운 LocalDateTime 생성
        LocalDateTime randomDateTime = currentDateTime.plus(randomSeconds, ChronoUnit.SECONDS);

        return randomDateTime;
    }
    
    public static LocalDateTime generateRandomLocalDateTimeFormTo(int from, int to) {
        // 현재 시간
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 랜덤한 초(0~59)와 분(0~59)을 생성
        Random random = new Random();
        int randomSeconds = random.nextInt(from, to);

        // 현재 시간에 랜덤한 초와 분을 더하여 새로운 LocalDateTime 생성
        LocalDateTime randomDateTime = currentDateTime.plus(randomSeconds, ChronoUnit.SECONDS);

        return randomDateTime;
    }
    
    public static LocalDateTime generateRandomLocalDateTimeFormToAddMillisec(int from, int to) {
        // 현재 시간
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 랜덤한 초(0~59)와 분(0~59)을 생성
        Random random = new Random();
        int randomSeconds = random.nextInt(from, to);

        // 현재 시간에 랜덤한 초와 분을 더하여 새로운 LocalDateTime 생성
        LocalDateTime randomDateTime = currentDateTime.plus(randomSeconds, ChronoUnit.MILLIS);

        return randomDateTime;
    }
}
