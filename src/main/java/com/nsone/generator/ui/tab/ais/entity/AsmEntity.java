package com.nsone.generator.ui.tab.ais.entity;

import java.time.LocalDateTime;

import org.quartz.JobDetail;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.nsone.generator.ui.tab.ais.entity.event.change.AsmEntityChangeStartTimeEvent;
import com.nsone.generator.ui.tab.ais.enums.AsmTdmaType;
import com.nsone.generator.util.RandomGenerator;

@Component
public class AsmEntity {
	//
	private final ApplicationEventPublisher eventPublisher;
	
	private LocalDateTime startTime;
	private JobDetail asmStartTimeJob;
	private char channel;
	private int slotCount = 3;
	
	

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
	
}
