package com.all4land.generator.ui.tab.ais.entity.event.change;

import org.springframework.context.ApplicationEvent;

import com.all4land.generator.ui.tab.ais.entity.GlobalEntityManager;

public class MmsiEntitySlotTimeChangeEvent extends ApplicationEvent {
	//
	private static final long serialVersionUID = -5773814074816459460L;
	//
	private String propertyName;
    private Object oldValue;
    private Object newValue;
    private Object clazz;
    private GlobalEntityManager globalEntityManager;

    public MmsiEntitySlotTimeChangeEvent(Object source, String propertyName, Object oldValue, Object newValue, Object clazz
    		, GlobalEntityManager globalEntityManager) {
        super(source);
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.clazz = clazz;
        this.globalEntityManager = globalEntityManager;
    }

    
	public GlobalEntityManager getGlobalEntityManager() {
		return globalEntityManager;
	}


	public void setGlobalEntityManager(GlobalEntityManager globalEntityManager) {
		this.globalEntityManager = globalEntityManager;
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