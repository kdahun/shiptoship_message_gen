package com.nsone.generator.ui.tab.ais.entity.event.change;

import org.springframework.context.ApplicationEvent;

import com.nsone.generator.ui.tab.ais.entity.GlobalSlotNumber;

public class ToggleSlotNumberEvent extends ApplicationEvent {
	//
	private static final long serialVersionUID = -3419341459564288325L;

	private GlobalSlotNumber globalSlotNumber;
	
	public ToggleSlotNumberEvent(Object source, GlobalSlotNumber globalSlotNumber) {
		super(source);
		// TODO Auto-generated constructor stub
		this.globalSlotNumber = globalSlotNumber;
	}

	public GlobalSlotNumber getGlobalSlotNumber() {
		return globalSlotNumber;
	}

	public void setGlobalSlotNumber(GlobalSlotNumber globalSlotNumber) {
		this.globalSlotNumber = globalSlotNumber;
	}

}
