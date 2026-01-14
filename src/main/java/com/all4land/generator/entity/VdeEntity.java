package com.all4land.generator.entity;

import java.time.LocalDateTime;

import org.quartz.JobDetail;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.event.VdeEntityChangeStartTimeEvent;
import com.all4land.generator.util.RandomGenerator;

@Component
public class VdeEntity {
	//
	private final ApplicationEventPublisher eventPublisher;
	private LocalDateTime startTime;
	private JobDetail vdeStartTimeJob;
	private char channel;
	private int tdma;
	
	public VdeEntity(ApplicationEventPublisher eventPublisher) {
		//
		this.eventPublisher = eventPublisher;
		this.channel = RandomGenerator.generateRandomChannel();
		this.tdma = RandomGenerator.generateRandomIntFromTo(1, 5);
	}
	
	
	
	public int getTdma() {
		return tdma;
	}



	public void setTdma(int tdma) {
		this.tdma = tdma;
	}



	public void setChannel(char channel) {
		this.channel = channel;
	}



	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime, MmsiEntity mmsiEntity) {
		//
		VdeEntityChangeStartTimeEvent event = new VdeEntityChangeStartTimeEvent(this, this.startTime, startTime, mmsiEntity, this);
		this.startTime = startTime;
		eventPublisher.publishEvent(event);
	}

	public JobDetail getVdeStartTimeJob() {
		return vdeStartTimeJob;
	}

	public void setVdeStartTimeJob(JobDetail vdeStartTimeJob) {
		this.vdeStartTimeJob = vdeStartTimeJob;
	}
	
	public char getChannel() {
		//
		return this.channel;
	}
}




