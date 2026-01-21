package com.all4land.generator.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

	private AsmTdmaType asmTdmaType;
	
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
		if (!this.destMMSIList.contains(destMMSI)) {
			this.destMMSIList.add(destMMSI);
			System.out.println("[DEBUG] ASM destMMSI 추가 - destMMSI: " + destMMSI + 
					", 리스트 크기: " + this.destMMSIList.size());
		} else {
			System.out.println("[DEBUG] ASM destMMSI 중복 - destMMSI: " + destMMSI);
		}
	}

	/**
	 * destMMSI를 리스트에서 제거
	 * @param destMMSI 제거할 목적지 MMSI
	 * @return 제거 성공 여부
	 */
	public boolean removeDestMMSI(long destMMSI) {
		if (this.destMMSIList.remove(destMMSI)) {
			System.out.println("[DEBUG] ASM destMMSI 제거 - destMMSI: " + destMMSI + 
					", 리스트 크기: " + this.destMMSIList.size());
			return true;
		} else {
			System.out.println("[DEBUG] ASM destMMSI 없음 - destMMSI: " + destMMSI);
			return false;
		}
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
	
}




