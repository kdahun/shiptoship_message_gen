package com.all4land.generator.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.quartz.JobDetail;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.enums.AsmTdmaType;
import com.all4land.generator.entity.event.AsmEntityChangeStartTimeEvent;
import com.all4land.generator.util.RandomGenerator;

@Component
public class AsmEntity {
	//
	private final ApplicationEventPublisher eventPublisher;
	
	private LocalDateTime startTime;
	private JobDetail asmStartTimeJob;
	private char channel;
	private int slotCount = 3;
	
	// destMMSI 리스트 (thread-safe)
	private List<Long> destMMSIList = new CopyOnWriteArrayList<>();
	
	// 각 destMMSI에 대한 asmPeriod 매핑 (thread-safe)
	private Map<Long, String> destMMSIAsmPeriodMap = new ConcurrentHashMap<>();

	private AsmTdmaType asmTdmaType;
	
	// ASM 메시지 전송 주기: "0"=단발 메시지, "4"~"360"=초 단위 주기 (기본값 "0")
	private String asmPeriod = "0";
	
	public AsmEntity(ApplicationEventPublisher eventPublisher) {
		//
		this.eventPublisher = eventPublisher;
		this.channel = RandomGenerator.generateRandomChannel();
	}
	
	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime, MmsiEntity mmsiEntity) {
		//
		AsmEntityChangeStartTimeEvent event = new AsmEntityChangeStartTimeEvent(this, this.startTime, startTime, mmsiEntity, this);
		this.startTime = startTime;
		eventPublisher.publishEvent(event);
	}

	public JobDetail getAsmStartTimeJob() {
		return asmStartTimeJob;
	}

	public void setAsmStartTimeJob(JobDetail asmStartTimeJob) {
		this.asmStartTimeJob = asmStartTimeJob;
	}
	
	public char getChannel() {
		//
		return this.channel;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(int slotCount) {
		this.slotCount = slotCount;
	}

	public void setChannel(char channel) {
		this.channel = channel;
	}
	
	/**
	 * destMMSI를 리스트에 추가
	 * @param destMMSI 추가할 목적지 MMSI
	 */
	public void addDestMMSI(long destMMSI) {
		addDestMMSI(destMMSI, "0"); // 기본값은 "0" (단발 메시지)
	}
	
	/**
	 * destMMSI를 리스트에 추가 (asmPeriod와 함께)
	 * @param destMMSI 추가할 목적지 MMSI
	 * @param asmPeriod "0"=단발 메시지, "4"~"360"=초 단위 주기
	 */
	public void addDestMMSI(long destMMSI, String asmPeriod) {
		// asmPeriod 검증 및 정규화
		String validatedPeriod = validateAsmPeriod(asmPeriod);
		
		if (!this.destMMSIList.contains(destMMSI)) {
			this.destMMSIList.add(destMMSI);
			this.destMMSIAsmPeriodMap.put(destMMSI, validatedPeriod);
			System.out.println("[DEBUG] ASM destMMSI 추가 - destMMSI: " + destMMSI + 
					", asmPeriod: " + this.destMMSIAsmPeriodMap.get(destMMSI) + 
					", 리스트 크기: " + this.destMMSIList.size());
		} else {
			// 이미 존재하는 경우 asmPeriod만 업데이트
			this.destMMSIAsmPeriodMap.put(destMMSI, validatedPeriod);
			System.out.println("[DEBUG] ASM destMMSI 중복 - destMMSI: " + destMMSI + 
					", asmPeriod 업데이트: " + this.destMMSIAsmPeriodMap.get(destMMSI));
		}
	}

	/**
	 * destMMSI를 리스트에서 제거
	 * @param destMMSI 제거할 목적지 MMSI
	 * @return 제거 성공 여부
	 */
	public boolean removeDestMMSI(long destMMSI) {
		if (this.destMMSIList.remove(destMMSI)) {
			this.destMMSIAsmPeriodMap.remove(destMMSI);
			System.out.println("[DEBUG] ASM destMMSI 제거 - destMMSI: " + destMMSI + 
					", 리스트 크기: " + this.destMMSIList.size());
			return true;
		} else {
			System.out.println("[DEBUG] ASM destMMSI 없음 - destMMSI: " + destMMSI);
			return false;
		}
	}
	
	/**
	 * 특정 destMMSI의 asmPeriod 반환
	 * @param destMMSI 목적지 MMSI
	 * @return asmPeriod ("0" 또는 "4"~"360"), 없으면 null
	 */
	public String getAsmPeriodForDestMMSI(long destMMSI) {
		return this.destMMSIAsmPeriodMap.get(destMMSI);
	}
	
	/**
	 * asmPeriod=0인 destMMSI만 리스트에서 제거
	 * @return 제거된 destMMSI 개수
	 */
	public int removeDestMMSIWithAsmPeriod0() {
		int removedCount = 0;
		List<Long> toRemove = new ArrayList<>();
		
		// asmPeriod=0인 destMMSI 찾기
		for (Long destMMSI : this.destMMSIList) {
			String asmPeriod = this.destMMSIAsmPeriodMap.get(destMMSI);
			if ("0".equals(asmPeriod)) {
				toRemove.add(destMMSI);
			}
		}
		
		// 제거
		for (Long destMMSI : toRemove) {
			if (this.destMMSIList.remove(destMMSI)) {
				this.destMMSIAsmPeriodMap.remove(destMMSI);
				removedCount++;
				System.out.println("[DEBUG] ASM destMMSI 제거 (asmPeriod=0) - destMMSI: " + destMMSI);
			}
		}
		
		if (removedCount > 0) {
			System.out.println("[DEBUG] ASM asmPeriod=0인 destMMSI 제거 완료 - 제거 개수: " + removedCount + 
					", 남은 리스트 크기: " + this.destMMSIList.size());
		}
		
		return removedCount;
	}

	/**
	 * destMMSI 리스트 반환
	 * @return destMMSI 리스트
	 */
	public List<Long> getDestMMSIList() {
		return new ArrayList<>(this.destMMSIList); // 방어적 복사
	}

	/**
	 * destMMSI 리스트가 비어있지 않은지 확인
	 * @return 리스트가 비어있지 않으면 true
	 */
	public boolean hasDestMMSI() {
		return !this.destMMSIList.isEmpty();
	}
	
	/**
	 * ASM 메시지 전송 주기 반환
	 * @return "0"=단발 메시지, "4"~"360"=초 단위 주기
	 */
	public String getAsmPeriod() {
		return asmPeriod;
	}

	/**
	 * ASM 메시지 전송 주기 설정
	 * @param asmPeriod "0"=단발 메시지, "4"~"360"=초 단위 주기
	 */
	public void setAsmPeriod(String asmPeriod) {
		this.asmPeriod = validateAsmPeriod(asmPeriod);
		System.out.println("[DEBUG] ASM Period 설정: " + this.asmPeriod + 
				" (" + ("0".equals(this.asmPeriod) ? "단발" : this.asmPeriod + "초 주기") + ")");
	}
	
	/**
	 * asmPeriod 값 검증 및 정규화
	 * @param asmPeriod 검증할 asmPeriod 값
	 * @return 검증된 asmPeriod 값 ("0" 또는 "4"~"360"), 유효하지 않으면 "0"
	 */
	private String validateAsmPeriod(String asmPeriod) {
		if (asmPeriod == null || asmPeriod.isEmpty()) {
			return "0";
		}
		
		// "0"은 단발 메시지로 허용
		if ("0".equals(asmPeriod)) {
			return "0";
		}
		
		// 숫자로 변환 시도
		try {
			int period = Integer.parseInt(asmPeriod);
			// 4~360 범위 검증
			if (period >= 4 && period <= 360) {
				return String.valueOf(period);
			} else {
				System.out.println("[DEBUG] ⚠️ ASM Period 범위 초과: " + period + " (4~360 범위여야 함), 기본값 0으로 설정");
				return "0";
			}
		} catch (NumberFormatException e) {
			// 숫자가 아닌 경우 (예: 기존 "1" 값)
			System.out.println("[DEBUG] ⚠️ ASM Period 형식 오류: " + asmPeriod + " (숫자여야 함), 기본값 0으로 설정");
			return "0";
		}
	}
	
}




