package com.nsone.generator.ui.tab.ais.entity.event.change;

import org.springframework.context.ApplicationEvent;

public class MmsiEntityChangeStartTimeEvent extends ApplicationEvent {
	//
	private static final long serialVersionUID = 2860936899140632816L;
	
	private String propertyName;
    private Object oldValue;
    private Object newValue;
    private Object clazz;

    public MmsiEntityChangeStartTimeEvent(Object source, String propertyName, Object oldValue, Object newValue, Object clazz) {
        super(source);
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.clazz = clazz;
    }

	public Object getClazz() {
		return clazz;
	}

	public void setClazz(Object clazz) {
		this.clazz = clazz;
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

}