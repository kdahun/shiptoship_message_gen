package com.all4land.generator.entity.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import com.all4land.generator.entity.AsmEntity;
import com.all4land.generator.entity.MmsiEntity;

public class AsmEntityChangeStartTimeEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9211127982018589838L;

	private LocalDateTime oldValue;
	private LocalDateTime newValue;
	private MmsiEntity mmsiEntity;
	private AsmEntity asmEntity;
	
	public AsmEntityChangeStartTimeEvent(Object source, LocalDateTime oldValue, LocalDateTime newValue, MmsiEntity mmsiEntity, AsmEntity asmEntity) {
		super(source);
		// TODO Auto-generated constructor stub
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.mmsiEntity = mmsiEntity;
		this.asmEntity = asmEntity;
	}

	
	
	public AsmEntity getAsmEntity() {
		return asmEntity;
	}



	public void setAsmEntity(AsmEntity asmEntity) {
		this.asmEntity = asmEntity;
	}



	public LocalDateTime getOldValue() {
		return oldValue;
	}

	public void setOldValue(LocalDateTime oldValue) {
		this.oldValue = oldValue;
	}

	public LocalDateTime getNewValue() {
		return newValue;
	}

	public void setNewValue(LocalDateTime newValue) {
		this.newValue = newValue;
	}

	public MmsiEntity getMmsiEntity() {
		return mmsiEntity;
	}

	public void setMmsiEntity(MmsiEntity mmsiEntity) {
		this.mmsiEntity = mmsiEntity;
	}

	
}




