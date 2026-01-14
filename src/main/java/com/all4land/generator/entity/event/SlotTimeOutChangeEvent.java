package com.all4land.generator.entity.event;

import org.springframework.context.ApplicationEvent;

import com.all4land.generator.entity.MmsiEntity;

public class SlotTimeOutChangeEvent extends ApplicationEvent {

	private static final long serialVersionUID = -604897380289147197L;
	
	private char channel;
	private MmsiEntity mmsiEntity;

	public SlotTimeOutChangeEvent(Object source, char channel, MmsiEntity mmsiEntity) {
		super(source);
		
		this.channel = channel;
		this.mmsiEntity = mmsiEntity;
	}

	public MmsiEntity getMmsiEntity() {
		return mmsiEntity;
	}

	public void setMmsiEntity(MmsiEntity mmsiEntity) {
		this.mmsiEntity = mmsiEntity;
	}

	public char getChannel() {
		return channel;
	}

	public void setChannel(char channel) {
		this.channel = channel;
	}

	
}




