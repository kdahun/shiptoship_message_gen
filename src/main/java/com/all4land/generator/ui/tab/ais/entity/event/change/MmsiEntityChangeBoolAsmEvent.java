package com.all4land.generator.ui.tab.ais.entity.event.change;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;

public class MmsiEntityChangeBoolAsmEvent extends ApplicationEvent {
	//
	private static final long serialVersionUID = -5028465007019400215L;

	private String propertyName;
    private Object oldValue;
    private Object newValue;
    private MmsiEntity mmsiEntity;
    private LocalDateTime time;
	
	public MmsiEntityChangeBoolAsmEvent(Object source, String propertyName, Object oldValue, Object newValue
			, MmsiEntity mmsiEntity, LocalDateTime time) {
		super(source);
		// TODO Auto-generated constructor stub
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.mmsiEntity = mmsiEntity;
		this.time = time;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public MmsiEntity getMmsiEntity() {
		return mmsiEntity;
	}

	public void setMmsiEntity(MmsiEntity mmsiEntity) {
		this.mmsiEntity = mmsiEntity;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

}
