package com.all4land.generator.ui.tab.ais.model;


import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Component;

import com.all4land.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TcpTargetClientTableModel extends DefaultTableModel {
	//
	private static final long serialVersionUID = -8753731243411396956L;

	private static final String[] columnNames = new String[] { "Desc", "IP", "Port", "AIS", "ASM", "TSQ" };
	
	private List<TcpTargetClientInfoEntity> tcpTargetClientInfoEntitys; 
	
	TcpTargetClientTableModel() {
		//
		super(columnNames, 0);
	}
	
	public void setTcpTargetClientTableEntitys(List<TcpTargetClientInfoEntity> tcpTargetClientInfoEntitys) {
		//
		this.tcpTargetClientInfoEntitys = tcpTargetClientInfoEntitys;
		for(TcpTargetClientInfoEntity tcpTargetClientInfoEntity : this.tcpTargetClientInfoEntitys) {
			//
			this.addRow(tcpTargetClientInfoEntity.rowData());
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
            switch (column) {
                case 3 -> {
                    //
                    this.tcpTargetClientInfoEntitys.get(row).setAis((boolean) value);
                    super.setValueAt(value, row, column);
                }
                case 4 -> {
                    //
                    this.tcpTargetClientInfoEntitys.get(row).setAsm((boolean) value);
                    super.setValueAt(value, row, column);
                }
                case 5 -> {
                    //
                    this.tcpTargetClientInfoEntitys.get(row).setTsq((boolean) value);
                    super.setValueAt(value, row, column);
                }
                default -> {
                }
            }
	}
	
}
