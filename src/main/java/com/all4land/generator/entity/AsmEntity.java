package com.all4land.generator.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.quartz.JobDetail;
import org.springframework.context.ApplicationEventPublisher;

import com.all4land.generator.entity.enums.AsmTdmaType;
import com.all4land.generator.entity.event.AsmEntityChangeStartTimeEvent;
import com.all4land.generator.util.RandomGenerator;

public class AsmEntity {
	//
	private final ApplicationEventPublisher eventPublisher;
	
	private LocalDateTime startTime;
	private JobDetail asmStartTimeJob;
	private char channel;
	private int slotCount = 3;
	
	// testMMSI 리스트 (thread-safe)
	private List<Long> testMMSIList = new CopyOnWriteArrayList<>();
	
	// 각 testMMSI에 대한 asmPeriod 매핑 (thread-safe)
	private Map<Long, String> testMMSIAsmPeriodMap = new ConcurrentHashMap<>();

	private AsmTdmaType asmTdmaType;
	
	// ASM 메시지 전송 주기: "0"=단발 메시지, "4"~"360"=초 단위 주기 (기본값 "0")
	private String asmPeriod = "0";
	
	// ASM 서비스 고유 식별자 (예: "asm_440115678_0")
	private String serviceId;
	
	public AsmEntity(ApplicationEventPublisher eventPublisher) {
		//
		this.eventPublisher = eventPublisher;
		this.channel = RandomGenerator.generateRandomChannel();
	}
	
	public AsmEntity(ApplicationEventPublisher eventPublisher, String serviceId) {
		//
		this.eventPublisher = eventPublisher;
		this.channel = RandomGenerator.generateRandomChannel();
		this.serviceId = serviceId;
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
	 * testMMSI를 리스트에 추가
	 * @param testMMSI 추가할 목적지 MMSI
	 */
	public void addTestMMSI(long testMMSI) {
		addTestMMSI(testMMSI, "0"); // 기본값은 "0" (단발 메시지)
	}
	
	/**
	 * testMMSI를 리스트에 추가 (asmPeriod와 함께)
	 * @param testMMSI 추가할 목적지 MMSI
	 * @param asmPeriod "0"=단발 메시지, "4"~"360"=초 단위 주기
	 */
	public void addTestMMSI(long testMMSI, String asmPeriod) {
		// asmPeriod 검증 및 정규화
		String validatedPeriod = validateAsmPeriod(asmPeriod);
		
		if (!this.testMMSIList.contains(testMMSI)) {
			this.testMMSIList.add(testMMSI);
			this.testMMSIAsmPeriodMap.put(testMMSI, validatedPeriod);
			System.out.println("[DEBUG] ASM testMMSI 추가 - testMMSI: " + testMMSI + 
					", asmPeriod: " + this.testMMSIAsmPeriodMap.get(testMMSI) + 
					", 리스트 크기: " + this.testMMSIList.size());
		} else {
			// 이미 존재하는 경우 asmPeriod만 업데이트
			this.testMMSIAsmPeriodMap.put(testMMSI, validatedPeriod);
			System.out.println("[DEBUG] ASM testMMSI 중복 - testMMSI: " + testMMSI + 
					", asmPeriod 업데이트: " + this.testMMSIAsmPeriodMap.get(testMMSI));
		}
	}

	/**
	 * testMMSI를 리스트에서 제거
	 * @param testMMSI 제거할 목적지 MMSI
	 * @return 제거 성공 여부
	 */
	public boolean removeTestMMSI(long testMMSI) {
		if (this.testMMSIList.remove(testMMSI)) {
			this.testMMSIAsmPeriodMap.remove(testMMSI);
			System.out.println("[DEBUG] ASM testMMSI 제거 - testMMSI: " + testMMSI + 
					", 리스트 크기: " + this.testMMSIList.size());
			return true;
		} else {
			System.out.println("[DEBUG] ASM testMMSI 없음 - testMMSI: " + testMMSI);
			return false;
		}
	}
	
	/**
	 * 특정 testMMSI의 asmPeriod 반환
	 * @param testMMSI 목적지 MMSI
	 * @return asmPeriod ("0" 또는 "4"~"360"), 없으면 null
	 */
	public String getAsmPeriodForTestMMSI(long testMMSI) {
		return this.testMMSIAsmPeriodMap.get(testMMSI);
	}
	
	/**
	 * asmPeriod=0인 testMMSI만 리스트에서 제거
	 * @return 제거된 testMMSI 개수
	 */
	public int removeTestMMSIWithAsmPeriod0() {
		int removedCount = 0;
		List<Long> toRemove = new ArrayList<>();
		
		// asmPeriod=0인 testMMSI 찾기
		for (Long testMMSI : this.testMMSIList) {
			String asmPeriod = this.testMMSIAsmPeriodMap.get(testMMSI);
			if ("0".equals(asmPeriod)) {
				toRemove.add(testMMSI);
			}
		}
		
		// 제거
		for (Long testMMSI : toRemove) {
			if (this.testMMSIList.remove(testMMSI)) {
				this.testMMSIAsmPeriodMap.remove(testMMSI);
				removedCount++;
				System.out.println("[DEBUG] ASM testMMSI 제거 (asmPeriod=0) - testMMSI: " + testMMSI);
			}
		}
		
		if (removedCount > 0) {
			System.out.println("[DEBUG] ASM asmPeriod=0인 testMMSI 제거 완료 - 제거 개수: " + removedCount + 
					", 남은 리스트 크기: " + this.testMMSIList.size());
		}
		
		return removedCount;
	}

	/**
	 * testMMSI 리스트 반환
	 * @return testMMSI 리스트
	 */
	public List<Long> getTestMMSIList() {
		return new ArrayList<>(this.testMMSIList); // 방어적 복사
	}

	/**
	 * testMMSI 리스트가 비어있지 않은지 확인
	 * @return 리스트가 비어있지 않으면 true
	 */
	public boolean hasTestMMSI() {
		return !this.testMMSIList.isEmpty();
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
	
	/**
	 * ASM 서비스 고유 식별자 반환
	 * @return serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}
	
	/**
	 * ASM 서비스 고유 식별자 설정
	 * @param serviceId 서비스 ID
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
}




