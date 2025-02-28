package com.all4land.generator.ui.tab.ais.model;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Component;

import com.all4land.generator.ui.tab.ais.entity.UdpTargetClientTableEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UdpTargetClientTableModel extends DefaultTableModel {
	//
	private static final long serialVersionUID = -340735873324375554L;
	private static final String[] columnNames = new String[] { "Desc", "IP", "Port", "AIS", "ASM", "TSQ" };
	
	private List<UdpTargetClientTableEntity> udpTargetClientTableEntitys; 
	
	UdpTargetClientTableModel() {
		//
		super(columnNames, 0);
	}
	
	public void setUdpTargetClientTableEntitys(List<UdpTargetClientTableEntity> udpTargetClientTableEntitys) {
		//
		this.udpTargetClientTableEntitys = udpTargetClientTableEntitys;
		for(UdpTargetClientTableEntity udpTargetClientTableEntity : this.udpTargetClientTableEntitys) {
			//
			this.addRow(udpTargetClientTableEntity.rowData());
		}
	}
	
	public void addRow(Object[] rowData) {
		//
		super.addRow(rowData);
	}
	
	public void removeRow(int row) {
		//
		super.removeRow(row);
	}
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		//
		if (column == 0) {
			//
			this.udpTargetClientTableEntitys.get(row).setDesc(value.toString());
			super.setValueAt(value, row, column);
		} else if (column == 1) {
			//
			this.udpTargetClientTableEntitys.get(row).setIp(value.toString());
			super.setValueAt(value, row, column);
		} else if (column == 2) {
			//
			this.udpTargetClientTableEntitys.get(row).setPort(Integer.parseInt(value.toString()) );
			super.setValueAt(value, row, column);
		} else if (column == 3) {
			//
			this.udpTargetClientTableEntitys.get(row).setAis((boolean) value);
			super.setValueAt(value, row, column);
		} else if (column == 4) {
			//
			this.udpTargetClientTableEntitys.get(row).setAsm((boolean) value);
			super.setValueAt(value, row, column);
		} else if (column == 5) {
			//
			this.udpTargetClientTableEntitys.get(row).setTsq((boolean) value);
			super.setValueAt(value, row, column);
		}
	}
	
}
