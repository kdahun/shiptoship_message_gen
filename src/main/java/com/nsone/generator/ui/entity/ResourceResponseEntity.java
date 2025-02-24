package com.nsone.generator.ui.entity;

import java.util.List;

import com.nsone.generator.ui.tab.ais.entity.ResourceInfoEntity;

import lombok.Data;

@Data
public class ResourceResponseEntity {
	//
	private int mode;
	private int startSlotNumber;
	private int responseSlotNumber;
	private int responseFrame;
	private List<ResourceInfoEntity> resourcePaintSlotList;
}