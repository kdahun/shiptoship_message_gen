package com.all4land.generator.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * JTable을 대체하여 슬롯 상태를 관리하는 클래스
 * 슬롯 번호를 키로 사용하여 점유 상태를 관리합니다.
 */
@Component
public class SlotStateManager {
	
	/**
	 * 슬롯 정보를 저장하는 클래스
	 */
	public static class SlotInfo {
		private int slotNumber;
		private Long mmsi;  // 점유한 선박의 MMSI, null이면 빈 슬롯
		private LocalDateTime occupiedTime;
		private LocalDateTime timeoutTime;
		private int row;
		private int col;
		private boolean channel; // true: A channel, false: B channel
		private String messageType; // "AIS", "ASM", "VDE" 등
		
		public SlotInfo() {
		}
		
		public SlotInfo(int slotNumber, int row, int col, boolean channel) {
			this.slotNumber = slotNumber;
			this.row = row;
			this.col = col;
			this.channel = channel;
		}
		
		public boolean isOccupied() {
			return mmsi != null;
		}
		
		public void occupy(Long mmsi, LocalDateTime occupiedTime, LocalDateTime timeoutTime, String messageType) {
			this.mmsi = mmsi;
			this.occupiedTime = occupiedTime;
			this.timeoutTime = timeoutTime;
			this.messageType = messageType;
		}
		
		public void release() {
			this.mmsi = null;
			this.occupiedTime = null;
			this.timeoutTime = null;
			this.messageType = null;
		}
		
		// Getters and Setters
		public int getSlotNumber() {
			return slotNumber;
		}
		
		public void setSlotNumber(int slotNumber) {
			this.slotNumber = slotNumber;
		}
		
		public Long getMmsi() {
			return mmsi;
		}
		
		public void setMmsi(Long mmsi) {
			this.mmsi = mmsi;
		}
		
		public LocalDateTime getOccupiedTime() {
			return occupiedTime;
		}
		
		public void setOccupiedTime(LocalDateTime occupiedTime) {
			this.occupiedTime = occupiedTime;
		}
		
		public LocalDateTime getTimeoutTime() {
			return timeoutTime;
		}
		
		public void setTimeoutTime(LocalDateTime timeoutTime) {
			this.timeoutTime = timeoutTime;
		}
		
		public int getRow() {
			return row;
		}
		
		public void setRow(int row) {
			this.row = row;
		}
		
		public int getCol() {
			return col;
		}
		
		public void setCol(int col) {
			this.col = col;
		}
		
		public boolean isChannel() {
			return channel;
		}
		
		public void setChannel(boolean channel) {
			this.channel = channel;
		}
		
		public String getMessageType() {
			return messageType;
		}
		
		public void setMessageType(String messageType) {
			this.messageType = messageType;
		}
	}
	
	// 슬롯 번호를 키로 사용하는 Map (0~2249)
	private final Map<Integer, SlotInfo> slotMap = new ConcurrentHashMap<>();
	
	/**
	 * 슬롯 초기화 (슬롯 번호와 좌표 정보 설정)
	 */
	public void initializeSlot(int slotNumber, int row, int col, boolean channel) {
		SlotInfo slot = slotMap.get(slotNumber);
		if (slot == null) {
			slot = new SlotInfo(slotNumber, row, col, channel);
			slotMap.put(slotNumber, slot);
		} else {
			slot.setRow(row);
			slot.setCol(col);
			slot.setChannel(channel);
		}
	}
	
	/**
	 * 슬롯 점유
	 * @param slotNumber 슬롯 번호
	 * @param mmsi 점유하는 선박의 MMSI
	 * @param occupiedTime 점유 시작 시간
	 * @param timeoutTime 타임아웃 시간
	 * @param messageType 메시지 타입 ("AIS", "ASM", "VDE")
	 * @return 점유 성공 여부
	 */
	public boolean occupySlot(int slotNumber, Long mmsi, LocalDateTime occupiedTime, 
			LocalDateTime timeoutTime, String messageType) {
		SlotInfo slot = slotMap.get(slotNumber);
		if (slot == null) {
			// 슬롯이 초기화되지 않았으면 기본값으로 생성
			int row = slotNumber / 32;
			int col = slotNumber % 32;
			slot = new SlotInfo(slotNumber, row, col, true);
			slotMap.put(slotNumber, slot);
		}
		
		if (!slot.isOccupied()) {
			slot.occupy(mmsi, occupiedTime, timeoutTime, messageType);
			return true;
		}
		return false; // 이미 점유됨
	}
	
	/**
	 * 슬롯 해제
	 */
	public void releaseSlot(int slotNumber) {
		SlotInfo slot = slotMap.get(slotNumber);
		if (slot != null) {
			slot.release();
		}
	}
	
	/**
	 * 특정 MMSI가 점유한 슬롯 해제
	 */
	public void releaseSlotsByMmsi(Long mmsi) {
		slotMap.values().stream()
			.filter(slot -> mmsi.equals(slot.getMmsi()))
			.forEach(SlotInfo::release);
	}
	
	/**
	 * 슬롯 점유 상태 확인
	 */
	public boolean isSlotOccupied(int slotNumber) {
		SlotInfo slot = slotMap.get(slotNumber);
		return slot != null && slot.isOccupied();
	}
	
	/**
	 * 슬롯 정보 조회
	 */
	public SlotInfo getSlotInfo(int slotNumber) {
		return slotMap.get(slotNumber);
	}
	
	/**
	 * SI 범위 내에서 빈 슬롯 찾기
	 * @param siMin 최소 슬롯 번호
	 * @param siMax 최대 슬롯 번호
	 * @return 찾은 슬롯 번호, 없으면 -1
	 */
	public int findAvailableSlot(int siMin, int siMax) {
		for (int slot = siMin; slot <= siMax && slot <= 2249; slot++) {
			SlotInfo info = slotMap.get(slot);
			if (info == null || !info.isOccupied()) {
				return slot;
			}
		}
		return -1; // 빈 슬롯 없음
	}
	
	/**
	 * 특정 MMSI가 점유한 슬롯 목록 조회
	 */
	public java.util.List<Integer> getSlotsByMmsi(Long mmsi) {
		return slotMap.entrySet().stream()
			.filter(entry -> mmsi.equals(entry.getValue().getMmsi()))
			.map(Map.Entry::getKey)
			.toList();
	}
	
	/**
	 * 타임아웃된 슬롯 해제
	 */
	public void releaseTimeoutSlots(LocalDateTime currentTime) {
		slotMap.values().stream()
			.filter(slot -> slot.isOccupied() && slot.getTimeoutTime() != null)
			.filter(slot -> currentTime.isAfter(slot.getTimeoutTime()))
			.forEach(SlotInfo::release);
	}
	
	/**
	 * 모든 슬롯 정보 초기화
	 */
	public void clear() {
		slotMap.clear();
	}
	
	/**
	 * 슬롯 맵 크기 조회
	 */
	public int size() {
		return slotMap.size();
	}
}

