package com.all4land.generator.ais;

import java.time.LocalDateTime;

import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.util.RandomGenerator;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage1;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.sentence.Vdm;
import dk.dma.enav.model.geometry.Position;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AisMessage1Util {
	//
	public static Vdm create(MmsiEntity mmsiEntity, int slotNumber) {
		//
		AisMessage1 msg1 = new AisMessage1();
		msg1.setRepeat(0);

		String strMmsi = String.valueOf(mmsiEntity.getMmsi());
		msg1.setUserId(Integer.parseInt(strMmsi));
//		msg1.setNavStatus(0);
//		msg1.setRot(0);
		// 랜덤한 속도와 방향 값 생성
		msg1.setSog(RandomGenerator.generateRandomIntFromTo(6, 7)*10);
//		msg1.setCog(RandomGenerator.generateRandomIntFromTo(0, 359));

		// latitude > 90 || latitude < -90
		// longitude > 180 || longitude < -180
		double latitude = RandomGenerator.generateRandomDouble(-90, 90, 0); // 위도
		double longitude = RandomGenerator.generateRandomDouble(-180, 180, 0); // 경도
		
		if(mmsiEntity.getSpeed() == 180) {
			//
			// Map<Integer, double[]> position = mmsiEntity.getPositions();
//			AisPosition pos = new AisPosition(Position.create(position.get(0)[0], position.get(0)[1]));
			
			AisPosition pos = new AisPosition(Position.create(latitude, longitude));
			
			msg1.setPos(pos);
			msg1.setCog(RandomGenerator.generateRandomIntFromTo(0, 359)*10);
			msg1.setSog(0);
			//System.out.println(mmsiEntity.getMmsi()+" " + position.get(0)[0] + " "+ position.get(0)[1]);
		}else {
			//
			// Map<Integer, double[]> position = mmsiEntity.getPositions();
			AisPosition pos = new AisPosition(Position.create(latitude, longitude));//new AisPosition(Position.create(position.get(mmsiEntity.getPositionsCnt())[0], position.get(mmsiEntity.getPositionsCnt())[1]));
			
//			try {
//				pos = new AisPosition(Position.create(position.get(mmsiEntity.getPositionsCnt())[0], position.get(mmsiEntity.getPositionsCnt())[1]));
//			}catch (Exception e) {
				// log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
//			}
			
			msg1.setPos(pos);
//			msg1.setSog((int)(position.get(mmsiEntity.getPositionsCnt())[2]*10));
//			msg1.setCog((int)(position.get(mmsiEntity.getPositionsCnt())[2]*10));
			
			
			msg1.setCog(RandomGenerator.generateRandomIntFromTo(0, 359)*10);
//			try {
//				msg1.setCog((int)(position.get(mmsiEntity.getPositionsCnt())[2]*10));
//			}catch (Exception e) {
//				System.out.println(">>>>>>>>>>>>>>>>"+mmsiEntity.getMmsi());
				// log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
//			}
			
//			System.out.println("COG " +position.get(mmsiEntity.getPositionsCnt())[2]+", "+ (int)(position.get(mmsiEntity.getPositionsCnt())[2]*10));
			mmsiEntity.plusPositionCnt();
			
//			System.out.println(mmsiEntity.getMmsi()+" " + position.get(mmsiEntity.getPositionsCnt())[0] 
//					+ " "+ position.get(mmsiEntity.getPositionsCnt())[1] + " "+ (int)position.get(mmsiEntity.getPositionsCnt())[2]);
		}
		
		
		
//		AisPosition pos = new AisPosition(Position.create(latitude, longitude));
//		msg1.setPos(pos);
		
		msg1.setTrueHeading(76);
		msg1.setUtcSec(42);
		msg1.setSpecialManIndicator(0);
		msg1.setSpare(0);
		msg1.setRaim(0);
		
		msg1.setNavStatus(RandomGenerator.generateRandomIntFromTo(0, 15));
		msg1.setRot(RandomGenerator.generateRandomIntFromTo(-128, 127));
		msg1.setPosAcc(RandomGenerator.generateRandomIntFromTo(0, 1));
		
		msg1.setSyncState(1);
		
		if(mmsiEntity.getSpeed() != 180) {
			msg1.setSlotTimeout(mmsiEntity.getSlotTimeOut());
		}
        msg1.setSubMessage(slotNumber);
		
//		msg1.setSubMessage(createCommState(0, slotTimeout, slotNumber));

		Vdm vdm = new Vdm();
		vdm.setTalker("AB");
		vdm.setFormatter("VDM");
		vdm.setTotal(1);
		vdm.setNum(1);
		vdm.setSequence(mmsiEntity.getAisMessageSequence());
		try {
			vdm.setMessageData(msg1);
		} catch (SixbitException e) {
			log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
		}
		
		if(mmsiEntity.getTargetChannel()){
			vdm.setChannel('A');
		}else {
			vdm.setChannel('B');
		}
		
		return vdm;
		// String encoded = vdm.getEncoded();
	}

	// CommState 값을 생성하는 메서드
	@SuppressWarnings("unused")
	private static int createCommState(int syncState, int slotTimeout, int subMessage) {
		//
		int rtnValue = 0;
		switch (slotTimeout) {
		case 0 -> {
                    int slotOffset = subMessage;
//			System.out.println("Slot Offset: " + slotOffset);
					rtnValue = (syncState << 17) | (slotTimeout << 14) | slotOffset;
                }
		case 1 -> {
                    int utcHour = LocalDateTime.now().getHour();
                    int utcMinute = LocalDateTime.now().getMinute();
//			System.out.println("UTC Hour: " + utcHour + ", UTC Minute: " + utcMinute);
					rtnValue = (syncState << 17) | (slotTimeout << 14) | encodeUTC(utcHour, utcMinute);
                }
		case 2, 4, 6 -> {
                    int slotNumber = subMessage;
//			System.out.println("Slot Number: " + slotNumber);
					rtnValue = (syncState << 17) | (slotTimeout << 14) | slotNumber;
                }
		case 3, 5, 7 -> {
                    int receivedStations = subMessage;
//			System.out.println("Received Stations: " + receivedStations);
					rtnValue = (syncState << 17) | (slotTimeout << 14) | receivedStations;
                }
                default -> {
                }
		}
//			System.out.println("Invalid Slot Timeout value");

		return rtnValue; //(syncState << 17) | (slotTimeout << 14) | subMessage;
	}

	// UTC 값을 인코딩하는 메서드
	private static int encodeUTC(int hour, int minute) {
		return (hour << 9) | (minute << 2);
	}

	// CommState 값을 출력하는 메서드
	@SuppressWarnings("unused")
	private static void printCommState(int commStateValue, String caseName) {
		System.out.println("----- " + caseName + " -----");
		System.out.println("CommState Value: " + commStateValue);

		int syncState = (commStateValue >> 17) & 0b11;
		int slotTimeout = (commStateValue >> 14) & 0b111;
		int subMessage = commStateValue & 0b11111111111111;

		System.out.println("Sync State: " + syncState);
		System.out.println("Slot Timeout: " + slotTimeout);
		System.out.println("Sub Message: " + subMessage);

		switch (slotTimeout) {
		case 0 -> {
                    int slotOffset = subMessage;
                    System.out.println("Slot Offset: " + slotOffset);
                }
		case 1 -> {
                    int utcHour = (subMessage >> 9) & 0b11111;
                    int utcMinute = (subMessage >> 2) & 0b111111;
                    System.out.println("UTC Hour: " + utcHour + ", UTC Minute: " + utcMinute);
                }
		case 2, 4, 6 -> {
                    int slotNumber = subMessage;
                    System.out.println("Slot Number: " + slotNumber);
                }
		case 3, 5, 7 -> {
                    int receivedStations = subMessage;
                    System.out.println("Received Stations: " + receivedStations);
                }
                default -> System.out.println("Invalid Slot Timeout value");
		}

		System.out.println();
	}
}
