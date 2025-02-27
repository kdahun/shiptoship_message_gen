package com.all4land.generator.ui.tab.ais.entity.event.change;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.ui.tab.ais.entity.VdeEntity;

public class VdeEntityChangeStartTimeEvent extends ApplicationEvent {
	//
	private static final long serialVersionUID = 8421494061238359169L;
	//
	private LocalDateTime oldValue;
	private LocalDateTime newValue;
	private MmsiEntity mmsiEntity;
	private VdeEntity vdeEntity;
	
	public VdeEntityChangeStartTimeEvent(Object source, LocalDateTime oldValue, LocalDateTime newValue, MmsiEntity mmsiEntity, VdeEntity vdeEntity) {
		super(source);
		// TODO Auto-generated constructor stub
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.mmsiEntity = mmsiEntity;
		this.vdeEntity = vdeEntity;
	}

	
	
	public VdeEntity getVdeEntity() {
		return vdeEntity;
	}



	public void setAsmEntity(VdeEntity vdeEntity) {
		this.vdeEntity = vdeEntity;
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
