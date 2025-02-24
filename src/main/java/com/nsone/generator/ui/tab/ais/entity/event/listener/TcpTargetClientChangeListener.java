package com.nsone.generator.ui.tab.ais.entity.event.listener;

import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nsone.generator.ui.tab.ais.entity.event.change.TcpTargetClientAddRowEvent;
import com.nsone.generator.ui.tab.ais.entity.event.change.TcpTargetClientRemoveRowEvent;
import com.nsone.generator.ui.tab.ais.model.TcpTargetClientTableModel;

@Component
public class TcpTargetClientChangeListener {
	//
	private final JTable tcpTargetClientJTableName;
	private final TcpTargetClientTableModel tcpTargetClientTableModel;
	
	public TcpTargetClientChangeListener(@Qualifier("tcpTargetClientJTableName") JTable tcpTargetClientJTableName
			, TcpTargetClientTableModel tcpTargetClientTableModel) {
		//
		this.tcpTargetClientJTableName = tcpTargetClientJTableName;
		this.tcpTargetClientTableModel = tcpTargetClientTableModel;
	}
	
	@EventListener
	public void onEventListener(TcpTargetClientAddRowEvent event) {
		//
		event.getTcpTargetClientInfoEntity();
		this.tcpTargetClientTableModel.addRow(event.getTcpTargetClientInfoEntity().rowData());
		this.tcpTargetClientJTableName.repaint();
	}
	
	@EventListener
	public void onEventListener(TcpTargetClientRemoveRowEvent event) {
		//
		this.tcpTargetClientTableModel.removeRow(event.getRow());
		this.tcpTargetClientJTableName.repaint();
	}
}
