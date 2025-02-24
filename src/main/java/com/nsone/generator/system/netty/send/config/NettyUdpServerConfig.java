package com.nsone.generator.system.netty.send.config;

import javax.swing.JTable;

import com.nsone.generator.ui.tab.ais.entity.GlobalSlotNumber;
import com.nsone.generator.ui.tab.ais.entity.UdpServerTableEntity;

import lombok.Data;

@Data
public class NettyUdpServerConfig {
	//
	private final UdpServerTableEntity udpServerTableEntity;
	private final GlobalSlotNumber globalSlotNumber;
	
	private final JTable currentFrameJTableNameUpper;
	private final JTable currentFrame1JTableNameUpper;
	private final JTable currentFrame2JTableNameUpper;
	private final JTable currentFrame3JTableNameUpper;
	private final JTable currentFrame4JTableNameUpper;
	private final JTable currentFrame5JTableNameUpper;
	private final JTable currentFrame6JTableNameUpper;
	private final JTable currentFrame7JTableNameUpper;
	private final JTable currentFrame8JTableNameUpper;
	private final JTable currentFrame9JTableNameUpper;
	private final JTable currentFrame10JTableNameUpper;
	
	private final JTable currentFrameJTableNameLower;
	private final JTable currentFrame1JTableNameLower;
	private final JTable currentFrame2JTableNameLower;
	private final JTable currentFrame3JTableNameLower;
	private final JTable currentFrame4JTableNameLower;
	private final JTable currentFrame5JTableNameLower;
	private final JTable currentFrame6JTableNameLower;
	private final JTable currentFrame7JTableNameLower;
	private final JTable currentFrame8JTableNameLower;
	private final JTable currentFrame9JTableNameLower;
	private final JTable currentFrame10JTableNameLower;
	
	public NettyUdpServerConfig(UdpServerTableEntity udpServerTableEntity
			, GlobalSlotNumber globalSlotNumber
			, JTable currentFrameJTableNameUpper
			, JTable currentFrame1JTableNameUpper
			, JTable currentFrame2JTableNameUpper
			, JTable currentFrame3JTableNameUpper
			, JTable currentFrame4JTableNameUpper
			, JTable currentFrame5JTableNameUpper
			, JTable currentFrame6JTableNameUpper
			, JTable currentFrame7JTableNameUpper
			, JTable currentFrame8JTableNameUpper
			, JTable currentFrame9JTableNameUpper
			, JTable currentFrame10JTableNameUpper
			
			, JTable currentFrameJTableNameLower
			, JTable currentFrame1JTableNameLower
			, JTable currentFrame2JTableNameLower
			, JTable currentFrame3JTableNameLower
			, JTable currentFrame4JTableNameLower
			, JTable currentFrame5JTableNameLower
			, JTable currentFrame6JTableNameLower
			, JTable currentFrame7JTableNameLower
			, JTable currentFrame8JTableNameLower
			, JTable currentFrame9JTableNameLower
			, JTable currentFrame10JTableNameLower
			) {
		//
		this.udpServerTableEntity = udpServerTableEntity;
		this.globalSlotNumber = globalSlotNumber;
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
		this.currentFrame8JTableNameUpper = currentFrame8JTableNameUpper;
		this.currentFrame9JTableNameUpper = currentFrame9JTableNameUpper;
		this.currentFrame10JTableNameUpper = currentFrame10JTableNameUpper;
		
		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
		this.currentFrame1JTableNameLower = currentFrame1JTableNameLower;
		this.currentFrame2JTableNameLower = currentFrame2JTableNameLower;
		this.currentFrame3JTableNameLower = currentFrame3JTableNameLower;
		this.currentFrame4JTableNameLower = currentFrame4JTableNameLower;
		this.currentFrame5JTableNameLower = currentFrame5JTableNameLower;
		this.currentFrame6JTableNameLower = currentFrame6JTableNameLower;
		this.currentFrame7JTableNameLower = currentFrame7JTableNameLower;
		this.currentFrame8JTableNameLower = currentFrame8JTableNameLower;
		this.currentFrame9JTableNameLower = currentFrame9JTableNameLower;
		this.currentFrame10JTableNameLower = currentFrame10JTableNameLower;
	}
	
	
}
