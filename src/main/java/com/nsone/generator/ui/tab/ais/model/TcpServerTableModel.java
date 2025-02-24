package com.nsone.generator.ui.tab.ais.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.nsone.generator.system.constant.SystemConstMessage;
import com.nsone.generator.system.netty.send.config.NettyTcpServerConfig;
import com.nsone.generator.system.netty.send.config.NettyUdpServerConfig;
import com.nsone.generator.system.netty.server.ServerFactory;
import com.nsone.generator.ui.tab.ais.entity.GlobalSlotNumber;
import com.nsone.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;
import com.nsone.generator.ui.tab.ais.entity.UdpServerTableEntity;
import com.nsone.generator.ui.tab.ais.entity.UdpTargetClientTableEntity;
import com.nsone.generator.ui.tab.ais.entity.TcpServerTableEntity;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TcpServerTableModel extends DefaultTableModel {
	//
	private static final long serialVersionUID = 8745725863140916169L; //
	private static final String[] columnNames = new String[] { "Desc", "server PORT", "OPEN"};
	
	private final ApplicationEventPublisher eventPublisher;
	
	private final TcpTargetClientTableModel tcpTargetClientTableModel;
	
	private List<TcpServerTableEntity> tcpServerTableEntitys = new ArrayList<>();
	
	private final ServerFactory serverFactory;
	
	private JTable currentFrameJTableNameUpper;
	private JTable currentFrame1JTableNameUpper;
	private JTable currentFrame2JTableNameUpper;
	private JTable currentFrame3JTableNameUpper;
	private JTable currentFrame4JTableNameUpper;
	private JTable currentFrame5JTableNameUpper;
	private JTable currentFrame6JTableNameUpper;
	private JTable currentFrame7JTableNameUpper;
	private JTable currentFrame8JTableNameUpper;
	private JTable currentFrame9JTableNameUpper;
	private JTable currentFrame10JTableNameUpper;
	
	private JTable currentFrameJTableNameLower;
	private JTable currentFrame1JTableNameLower;
	private JTable currentFrame2JTableNameLower;
	private JTable currentFrame3JTableNameLower;
	private JTable currentFrame4JTableNameLower;
	private JTable currentFrame5JTableNameLower;
	private JTable currentFrame6JTableNameLower;
	private JTable currentFrame7JTableNameLower;
	private JTable currentFrame8JTableNameLower;
	private JTable currentFrame9JTableNameLower;
	private JTable currentFrame10JTableNameLower;
	
	private GlobalSlotNumber globalSlotNumber;
	
	

	TcpServerTableModel(TcpTargetClientTableModel tcpTargetClientTableModel
			, ServerFactory serverFactory, ApplicationEventPublisher eventPublisher) {
		//
		super(columnNames, 0);
		this.tcpTargetClientTableModel = tcpTargetClientTableModel;
		this.serverFactory = serverFactory;
		this.eventPublisher = eventPublisher;
//		this.addInitRow();
	}
	
	public int getTargetRowCount(int parentSelectedRow) {
		//
		return this.tcpServerTableEntitys.get(parentSelectedRow).getTcpTargetClientInfoEntityCount();
	}
	
	public void rowSelectedEvent(int parentSelectedRow) {
		//
		this.tcpTargetClientTableModel.setRowCount(0); // remove all row
		
		for(TcpServerTableEntity tcpServerTableEntity : this.tcpServerTableEntitys) {
			//
			tcpServerTableEntity.setSelected(false);
		}
		this.tcpServerTableEntitys.get(parentSelectedRow).setSelected(true);
		
		List<TcpTargetClientInfoEntity> list = this.tcpServerTableEntitys.get(parentSelectedRow).getTcpTargetClientInfoEntitys();
		this.tcpTargetClientTableModel.setTcpTargetClientTableEntitys(list);
	}
	
	public void addTargetClientRow(int parentSelectedRow, Object[] newRow) {
		//
		TcpTargetClientInfoEntity tcpTargetClientInfoEntity = new TcpTargetClientInfoEntity();
		tcpTargetClientInfoEntity.setDesc(newRow[0].toString());
		tcpTargetClientInfoEntity.setIp( newRow[1].toString());
		tcpTargetClientInfoEntity.setPort((int) newRow[2]);
		
		this.tcpServerTableEntitys.get(parentSelectedRow).addTcpTargetClientInfoEntity(tcpTargetClientInfoEntity);
		this.tcpTargetClientTableModel.addRow(newRow);
	}
	
	public void addRow(Object[] rowData) {
		//
		TcpServerTableEntity tcpServerTableEntity = new TcpServerTableEntity(this.eventPublisher);
		tcpServerTableEntity.setDesc(rowData[0].toString());
		tcpServerTableEntity.setPort((int) rowData[1]);
		tcpServerTableEntity.setStatus((boolean) rowData[2]);

		this.tcpServerTableEntitys.add(tcpServerTableEntity);
		super.addRow(rowData);
	}
	
	public void removeRow(int parentSelectedRow) {
		//
		this.removeNettyServerTCPConfiguration(this.tcpServerTableEntitys.get(parentSelectedRow));
		
		List<TcpTargetClientInfoEntity> targetClients = this.tcpServerTableEntitys.get(parentSelectedRow).getTcpTargetClientInfoEntitys();
		Iterator<TcpTargetClientInfoEntity> iterator = targetClients.iterator();

        while (iterator.hasNext()) {
            try {
//                UdpTargetClientTableEntity client = iterator.next();
                iterator.remove();
            } catch (Exception e) {
                e.printStackTrace();
                // 필요하다면 사용자에게 오류 메시지를 전달합니다.
            }
        }
        
        this.tcpTargetClientTableModel.setRowCount(0); // remove all row
		
		this.tcpServerTableEntitys.remove(parentSelectedRow);
		super.removeRow(parentSelectedRow);
		
	}
	
	public void removeTargetClientRow(int parentSelectedRow, int targetRow) {
		//
		this.tcpServerTableEntitys.get(parentSelectedRow).getTcpTargetClientInfoEntitys().remove(targetRow);
		this.tcpTargetClientTableModel.removeRow(targetRow);
	}
	
	private void makeNettyServerTCPConfiguration(TcpServerTableEntity tcpServerTableEntity) {
		//
		NettyTcpServerConfig nettyTcpServerConfig = new NettyTcpServerConfig(tcpServerTableEntity, this.globalSlotNumber
				, this.currentFrameJTableNameUpper, this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper
				, this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper, this.currentFrame5JTableNameUpper
				, this.currentFrame6JTableNameUpper, this.currentFrame7JTableNameUpper, this.currentFrame8JTableNameUpper
				, this.currentFrame9JTableNameUpper, this.currentFrame10JTableNameUpper, this.currentFrameJTableNameLower
				, this.currentFrame1JTableNameLower, this.currentFrame2JTableNameLower, this.currentFrame3JTableNameLower
				, this.currentFrame4JTableNameLower, this.currentFrame5JTableNameLower, this.currentFrame6JTableNameLower
				, this.currentFrame7JTableNameLower, this.currentFrame8JTableNameLower, this.currentFrame9JTableNameLower
				, this.currentFrame10JTableNameLower);
		
		this.serverFactory.makeTcpServer(tcpServerTableEntity, nettyTcpServerConfig);
	}
	
	private void removeNettyServerTCPConfiguration(TcpServerTableEntity tcpServerTableEntity) {
		//
		this.serverFactory.removeTCPServer(tcpServerTableEntity);
	}
	
	public void sendAISMessage(String aisMessage, String vsiMessage) {
		//
		for(TcpServerTableEntity tcpServerTableEntity : this.tcpServerTableEntitys) {
			//
			if(tcpServerTableEntity.isStatus() && tcpServerTableEntity.getNettyServerTCPConfiguration() != null) {
				//
				StringBuilder sb = new StringBuilder();
				sb.append(aisMessage).append(SystemConstMessage.CRLF);
				sb.append(vsiMessage).append(SystemConstMessage.CRLF);
				for(TcpTargetClientInfoEntity tcpTargetClientInfoEntity : tcpServerTableEntity.getTcpTargetClientInfoEntitys()) {
					//
					if(tcpTargetClientInfoEntity.isAis()) {
						String ip = tcpTargetClientInfoEntity.getIp();
						int port = Integer.valueOf(tcpTargetClientInfoEntity.getPort());
						tcpServerTableEntity.getNettyServerTCPConfiguration().sendToClient(tcpTargetClientInfoEntity.getClientChannel()
								,ip, port, sb.toString());
					}
				}
			}
		}
	}
	
	public void sendASMMessage(String asmMessageAndVsiMessage) {
		//
		for(TcpServerTableEntity tcpServerTableEntity : this.tcpServerTableEntitys) {
			//
			if(tcpServerTableEntity.isStatus() && tcpServerTableEntity.getNettyServerTCPConfiguration() != null ) {
				//
				for(TcpTargetClientInfoEntity tcpTargetClientInfoEntity : tcpServerTableEntity.getTcpTargetClientInfoEntitys()) {
					//
					if(tcpTargetClientInfoEntity.isAsm()) {
						String ip = tcpTargetClientInfoEntity.getIp();
						int port = Integer.valueOf(tcpTargetClientInfoEntity.getPort());
						tcpServerTableEntity.getNettyServerTCPConfiguration().sendToClient(tcpTargetClientInfoEntity.getClientChannel()
								,ip, port, asmMessageAndVsiMessage);
					}
//					Channel ch = sendClientInfoEntity.getClientChannel();
//					String ip = tcpTargetClientInfoEntity.getIp();
//					int port = Integer.valueOf(tcpTargetClientInfoEntity.getPort());
//					tcpServerTableEntity.getNettyServerTCPConfiguration().sendToClient(tcpTargetClientInfoEntity.getClientChannel() ,ip, port, asmMessageAndVsiMessage);
				}
			}
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		//
		if (column == 0) {
			//
			this.tcpServerTableEntitys.get(row).setDesc(value.toString());
			super.setValueAt(value, row, column);
		} else if (column == 1) {
			//
			this.tcpServerTableEntitys.get(row).setPort(Integer.valueOf(value.toString()) );
			super.setValueAt(value, row, column);
		} else if (column == 2) {
			//
			this.tcpServerTableEntitys.get(row).setStatus((boolean) value);
			super.setValueAt(value, row, column);
			if((boolean) value) {
				this.makeNettyServerTCPConfiguration(this.tcpServerTableEntitys.get(row));
			}else {
				this.removeNettyServerTCPConfiguration(this.tcpServerTableEntitys.get(row));
			}
		}
	}

	public JTable getCurrentFrameJTableNameUpper() {
		return currentFrameJTableNameUpper;
	}

	public void setCurrentFrameJTableNameUpper(JTable currentFrameJTableNameUpper) {
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
	}

	public JTable getCurrentFrame1JTableNameUpper() {
		return currentFrame1JTableNameUpper;
	}

	public void setCurrentFrame1JTableNameUpper(JTable currentFrame1JTableNameUpper) {
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
	}

	public JTable getCurrentFrame2JTableNameUpper() {
		return currentFrame2JTableNameUpper;
	}

	public void setCurrentFrame2JTableNameUpper(JTable currentFrame2JTableNameUpper) {
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
	}

	public JTable getCurrentFrame3JTableNameUpper() {
		return currentFrame3JTableNameUpper;
	}

	public void setCurrentFrame3JTableNameUpper(JTable currentFrame3JTableNameUpper) {
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
	}

	public JTable getCurrentFrame4JTableNameUpper() {
		return currentFrame4JTableNameUpper;
	}

	public void setCurrentFrame4JTableNameUpper(JTable currentFrame4JTableNameUpper) {
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
	}

	public JTable getCurrentFrame5JTableNameUpper() {
		return currentFrame5JTableNameUpper;
	}

	public void setCurrentFrame5JTableNameUpper(JTable currentFrame5JTableNameUpper) {
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
	}

	public JTable getCurrentFrame6JTableNameUpper() {
		return currentFrame6JTableNameUpper;
	}

	public void setCurrentFrame6JTableNameUpper(JTable currentFrame6JTableNameUpper) {
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
	}

	public JTable getCurrentFrame7JTableNameUpper() {
		return currentFrame7JTableNameUpper;
	}

	public void setCurrentFrame7JTableNameUpper(JTable currentFrame7JTableNameUpper) {
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
	}

	public JTable getCurrentFrame8JTableNameUpper() {
		return currentFrame8JTableNameUpper;
	}

	public void setCurrentFrame8JTableNameUpper(JTable currentFrame8JTableNameUpper) {
		this.currentFrame8JTableNameUpper = currentFrame8JTableNameUpper;
	}

	public JTable getCurrentFrame9JTableNameUpper() {
		return currentFrame9JTableNameUpper;
	}

	public void setCurrentFrame9JTableNameUpper(JTable currentFrame9JTableNameUpper) {
		this.currentFrame9JTableNameUpper = currentFrame9JTableNameUpper;
	}

	public JTable getCurrentFrame10JTableNameUpper() {
		return currentFrame10JTableNameUpper;
	}

	public void setCurrentFrame10JTableNameUpper(JTable currentFrame10JTableNameUpper) {
		this.currentFrame10JTableNameUpper = currentFrame10JTableNameUpper;
	}

	public JTable getCurrentFrameJTableNameLower() {
		return currentFrameJTableNameLower;
	}

	public void setCurrentFrameJTableNameLower(JTable currentFrameJTableNameLower) {
		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
	}

	public JTable getCurrentFrame1JTableNameLower() {
		return currentFrame1JTableNameLower;
	}

	public void setCurrentFrame1JTableNameLower(JTable currentFrame1JTableNameLower) {
		this.currentFrame1JTableNameLower = currentFrame1JTableNameLower;
	}

	public JTable getCurrentFrame2JTableNameLower() {
		return currentFrame2JTableNameLower;
	}

	public void setCurrentFrame2JTableNameLower(JTable currentFrame2JTableNameLower) {
		this.currentFrame2JTableNameLower = currentFrame2JTableNameLower;
	}

	public JTable getCurrentFrame3JTableNameLower() {
		return currentFrame3JTableNameLower;
	}

	public void setCurrentFrame3JTableNameLower(JTable currentFrame3JTableNameLower) {
		this.currentFrame3JTableNameLower = currentFrame3JTableNameLower;
	}

	public JTable getCurrentFrame4JTableNameLower() {
		return currentFrame4JTableNameLower;
	}

	public void setCurrentFrame4JTableNameLower(JTable currentFrame4JTableNameLower) {
		this.currentFrame4JTableNameLower = currentFrame4JTableNameLower;
	}

	public JTable getCurrentFrame5JTableNameLower() {
		return currentFrame5JTableNameLower;
	}

	public void setCurrentFrame5JTableNameLower(JTable currentFrame5JTableNameLower) {
		this.currentFrame5JTableNameLower = currentFrame5JTableNameLower;
	}

	public JTable getCurrentFrame6JTableNameLower() {
		return currentFrame6JTableNameLower;
	}

	public void setCurrentFrame6JTableNameLower(JTable currentFrame6JTableNameLower) {
		this.currentFrame6JTableNameLower = currentFrame6JTableNameLower;
	}

	public JTable getCurrentFrame7JTableNameLower() {
		return currentFrame7JTableNameLower;
	}

	public void setCurrentFrame7JTableNameLower(JTable currentFrame7JTableNameLower) {
		this.currentFrame7JTableNameLower = currentFrame7JTableNameLower;
	}

	public JTable getCurrentFrame8JTableNameLower() {
		return currentFrame8JTableNameLower;
	}

	public void setCurrentFrame8JTableNameLower(JTable currentFrame8JTableNameLower) {
		this.currentFrame8JTableNameLower = currentFrame8JTableNameLower;
	}

	public JTable getCurrentFrame9JTableNameLower() {
		return currentFrame9JTableNameLower;
	}

	public void setCurrentFrame9JTableNameLower(JTable currentFrame9JTableNameLower) {
		this.currentFrame9JTableNameLower = currentFrame9JTableNameLower;
	}

	public JTable getCurrentFrame10JTableNameLower() {
		return currentFrame10JTableNameLower;
	}

	public void setCurrentFrame10JTableNameLower(JTable currentFrame10JTableNameLower) {
		this.currentFrame10JTableNameLower = currentFrame10JTableNameLower;
	}

	public GlobalSlotNumber getGlobalSlotNumber() {
		return globalSlotNumber;
	}

	public void setGlobalSlotNumber(GlobalSlotNumber globalSlotNumber) {
		this.globalSlotNumber = globalSlotNumber;
	}

	public List<TcpServerTableEntity> getTcpServerTableEntitys() {
		return tcpServerTableEntitys;
	}

	public void setTcpServerTableEntitys(List<TcpServerTableEntity> tcpServerTableEntitys) {
		this.tcpServerTableEntitys = tcpServerTableEntitys;
	}
	
	
	
//	
//	public void setSendjTextAreaName(JTextArea sendjTextAreaName) {
//		//
//		this.sendjTextAreaName = sendjTextAreaName;
//	}
	
//	private void addInitRow() {
//        // 테이블 모델에 빈 행 추가
//        Object[] rowData = { "저쪽  ", 9990, false, false, false, false, false};
//        this.addRow(rowData);
//        Object[] rowData1 = { "저쪽  ", 9991, false, false, false, false, false};
//        this.addRow(rowData1);
//    }
//
//	public void addRow(Object[] rowData) {
//		//
//		TcpServerTableEntity sendTableEntity = new TcpServerTableEntity();
//		sendTableEntity.setDesc(rowData[0].toString());
//		sendTableEntity.setPort((int) rowData[1]);
//		sendTableEntity.setStatus((boolean) rowData[2]);
//		sendTableEntity.setAis((boolean) rowData[3]);
//		sendTableEntity.setAsm((boolean) rowData[4]);
//		sendTableEntity.setVdeUp((boolean) rowData[5]);
//		sendTableEntity.setVdeLo((boolean) rowData[6]);
//
//		this.sendTableEntityLists.add(sendTableEntity);
//		super.addRow(rowData);
//	}
//	
//	public void removeRow(int selectedRow) {
//		//
//		if (selectedRow >= 0 && selectedRow < getRowCount()) {
//			//
//			this.sendTableEntityLists.remove(selectedRow);
//			super.removeRow(selectedRow);
//		}
//	}
//
//	@Override
//	public void setValueAt(Object value, int row, int column) {
//		//
//		if (column == 0) {
//			//
//			this.sendTableEntityLists.get(row).setDesc(value.toString());
//			super.setValueAt(value, row, column);
//		} else if (column == 1) {
//			//
//			this.sendTableEntityLists.get(row).setPort(Integer.valueOf(value.toString()));
//			super.setValueAt(value, row, column);
//		} else if (column == 2) {
//			//
//			this.sendTableEntityLists.get(row).setStatus((boolean)value);
//			super.setValueAt(value, row, column);
//			
//			if ((boolean) value) { // 체크가 될때
//				boolean chk = this.validationCheck(this.sendTableEntityLists.get(row));
//				if(chk) {
//					this.sendTableEntityLists.get(row).setStatus(false);
//					super.setValueAt(false, row, column);
//				}else {
//					//
////					if(this.sendTableEntityLists.get(row).getTcpUdp().equals("tcp")) {
//						this.makeNettyServerTCPConfiguration(this.sendTableEntityLists.get(row));
////					}else if(this.sendTableEntityLists.get(row).getTcpUdp().equals("tcp")) {
////						this.makeNettyServerUDPConfiguration(this.sendTableEntityLists.get(row));
////					}
//				}
//			}else { // 체크 해제 될때
////				if(this.sendTableEntityLists.get(row).getTcpUdp().equals("tcp")) {
//					this.removeNettyServerTCPConfiguration(this.sendTableEntityLists.get(row));
////				}else if(this.sendTableEntityLists.get(row).getTcpUdp().equals("tcp")) {
////					this.removeNettyServerUDPConfiguration(this.sendTableEntityLists.get(row));
////				}
//			}
//			
////			this.sendTableEntityLists.get(row).setStatus((boolean)value);
////			super.setValueAt(value, row, column);
////			//
////			boolean chk = this.validationCheck(this.sendTableEntityLists.get(row));
////			if(chk) {
////				this.sendTableEntityLists.get(row).setStatus(false);
////				super.setValueAt(false, row, column);
////			}else {
////				//
////				if(this.sendTableEntityLists.get(row).getTcpUdp().equals("tcp")) {
////					this.makeNettyServerTCPConfiguration(this.sendTableEntityLists.get(row));
////				}else if(this.sendTableEntityLists.get(row).getTcpUdp().equals("tcp")) {
////					this.makeNettyServerUDPConfiguration(this.sendTableEntityLists.get(row));
////				}
//				
////				if(startChk) {
////					// 연결 성공
////					this.makeSendService(this.sendTableEntityLists.get(row));
////					this.sendTableEntityLists.get(row).setStatus(true);
////					super.setValueAt(true, row, column);
////					SwingUtilities.invokeLater(() -> {
////						//
////						this.sendjTextAreaName
////								.append(SystemConstMessage.CRLF + "연결 되었습니다.");
////					});
////				}else {
////					// 연결 실패
////					this.sendTableEntityLists.get(row).setStatus(false);
////					super.setValueAt(false, row, column);
////					SwingUtilities.invokeLater(() -> {
////						//
////						this.sendjTextAreaName
////								.append(SystemConstMessage.CRLF + "연결 실패 되었습니다.");
////					});
////				}
////				this.makeSendService(this.sendTableEntityLists.get(row));
////				this.sendTableEntityLists.get(row).setStatus(true);
////				super.setValueAt(true, row, column);
////			}
//			
//		} else if (column == 3) {
//			//
//			this.sendTableEntityLists.get(row).setAis((boolean)value);
//			super.setValueAt(value, row, column);
//		} else if (column == 4) {
//			//
//			this.sendTableEntityLists.get(row).setAsm((boolean)value);
//			super.setValueAt(value, row, column);
//		} else if (column == 5) {
//			//
//			this.sendTableEntityLists.get(row).setVdeUp((boolean)value);
//			super.setValueAt(value, row, column);
//		} else if (column == 6) {
//			//
//			this.sendTableEntityLists.get(row).setVdeLo((boolean)value);
//			super.setValueAt(value, row, column);
//		} 
//	}
//	
//	private boolean validationCheck(TcpServerTableEntity sendTableEntity) {
//		//
//		boolean chk = false; 
//		if(sendTableEntity.getPort() <= 0) {
//			chk = true;
//		}
//		
//		return chk;
//	}
//	
//	private void makeNettyServerTCPConfiguration(TcpServerTableEntity sendTableEntity) {
//		//
//		NettyServerConfig nettyServerConfig = new NettyServerConfig(sendTableEntity, this.globalSlotNumber
//				, this.currentFrameJTableNameUpper, this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper
//				, this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper, this.currentFrame5JTableNameUpper
//				, this.currentFrame6JTableNameUpper, this.currentFrame7JTableNameUpper, this.currentFrame8JTableNameUpper
//				, this.currentFrame9JTableNameUpper, this.currentFrame10JTableNameUpper, this.currentFrameJTableNameLower
//				, this.currentFrame1JTableNameLower, this.currentFrame2JTableNameLower, this.currentFrame3JTableNameLower
//				, this.currentFrame4JTableNameLower, this.currentFrame5JTableNameLower, this.currentFrame6JTableNameLower
//				, this.currentFrame7JTableNameLower, this.currentFrame8JTableNameLower, this.currentFrame9JTableNameLower
//				, this.currentFrame10JTableNameLower);
//		
//		this.serverFactory.makeTcpServer(sendTableEntity, nettyServerConfig);
//	}
//	
////	private void makeNettyServerUDPConfiguration(SendTableEntity sendTableEntity) {
////		//
////		NettyServerConfig nettyServerConfig = new NettyServerConfig(sendTableEntity, this.globalSlotNumber
////				, this.currentFrameJTableNameUpper, this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper
////				, this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper, this.currentFrame5JTableNameUpper
////				, this.currentFrame6JTableNameUpper, this.currentFrame7JTableNameUpper, this.currentFrame8JTableNameUpper
////				, this.currentFrame9JTableNameUpper, this.currentFrame10JTableNameUpper, this.currentFrameJTableNameLower
////				, this.currentFrame1JTableNameLower, this.currentFrame2JTableNameLower, this.currentFrame3JTableNameLower
////				, this.currentFrame4JTableNameLower, this.currentFrame5JTableNameLower, this.currentFrame6JTableNameLower
////				, this.currentFrame7JTableNameLower, this.currentFrame8JTableNameLower, this.currentFrame9JTableNameLower
////				, this.currentFrame10JTableNameLower);
////		
////		this.serverFactory.makeUdpServer(sendTableEntity, nettyServerConfig);
////	}
//	
//	private void removeNettyServerTCPConfiguration(TcpServerTableEntity sendTableEntity) {
//		//
//		this.serverFactory.removeTcpServer(sendTableEntity);
//	}
//	
//	private void removeNettyServerUDPConfiguration(TcpServerTableEntity sendTableEntity) {
//		//
//		this.serverFactory.removeTcpServer(sendTableEntity);
//	}
//	
////	private boolean makeNettySocketService(SendTableEntity sendTableEntity) {
////		//
////		// tcp tcp bean 생성 및 연결 
////		NettySendSocketService newNettySendSocketService = null;
////		try {
////			//
////			newNettySendSocketService = (NettySendSocketService) BeanUtils.getBean(sendTableEntity.getIp() + sendTableEntity.getPort() + "");
////		} catch (Exception e) {
////			// TODO: handle exception
////		}
////		
////		if (newNettySendSocketService == null) {
////			//
////			BeanUtils.registerBean(sendTableEntity.getIp() + sendTableEntity.getPort() + "", NettySendSocketService.class);
////			newNettySendSocketService = (NettySendSocketService) BeanUtils.getBean(sendTableEntity.getIp() + sendTableEntity.getPort() + "");
////		}
////		sendTableEntity.setNettySendSocketService(newNettySendSocketService);
////		return newNettySendSocketService.startTcp(sendTableEntity.getIp(), sendTableEntity.getPort());
////	}
//	
////	private void makeSendService(SendTableEntity sendTableEntity) {
////		//
////		if(sendTableEntity.isStatus()) {
////			//
////			SendService newSendService = null;
////			try {
////				//
////				newSendService = (SendService) BeanUtils.getBean(sendTableEntity.getIp() + sendTableEntity.getPort() + "SendService");
////			} catch (Exception e) {
////				// TODO: handle exception
////				log.error(e.getMessage());
////			}
////			if (newSendService == null) {
////				//
////				BeanUtils.registerBean(sendTableEntity.getIp() + sendTableEntity.getPort() + "SendService", SendService.class);
////				newSendService = (SendService) BeanUtils.getBean(sendTableEntity.getIp() + sendTableEntity.getPort() + "SendService");	
////			}
////			sendTableEntity.setSendService(newSendService);
////			newSendService.setTcpClientChannel(sendTableEntity.getNettySendSocketService().getTcpClientChannel());
////			newSendService.setTcpBootstrap(sendTableEntity.getNettySendSocketService().getSendTcpClientBootstrap());
////			
////			log.info("SendService 동적빈 생성");
////		}else {
////			//
////			BeanUtils.removeBean(sendTableEntity.getIp() + sendTableEntity.getPort() + "SendService");
////			log.info("SendService 동적빈 삭제");
////		}
////	}
//	
//	public void sendAISMessage(String aisMessage, String vsiMessage) {
//		//
//		for(TcpServerTableEntity sendTableEntity : this.sendTableEntityLists) {
//			//
//			if(sendTableEntity.isAis() && sendTableEntity.isStatus() && sendTableEntity.getNettyServerTCPConfiguration() != null ) {
//				//
//				StringBuilder sb = new StringBuilder();
//				sb.append(aisMessage).append(SystemConstMessage.CRLF);
//				sb.append(vsiMessage).append(SystemConstMessage.CRLF);
//				for(TcpTargetClientInfoEntity sendClientInfoEntity : sendTableEntity.getSendClientInfoEntityLists()) {
//					//
//					Channel clientChannel = sendClientInfoEntity.getClientChannel();
//					String ip = sendClientInfoEntity.getIp();
//					int port = sendClientInfoEntity.getPort();
//					sendTableEntity.getNettyServerTCPConfiguration().sendToClient(clientChannel, ip, port, sb.toString());
//				}
//			}
//		}
//	}
//	
//	public void sendASMMessage(String asmMessageAndVsiMessage) {
//		//
//		for(TcpServerTableEntity sendTableEntity : this.sendTableEntityLists) {
//			//
//			if(sendTableEntity.isAsm() && sendTableEntity.isStatus() && sendTableEntity.getNettyServerTCPConfiguration() != null ) {
//				//
//				for(TcpTargetClientInfoEntity sendClientInfoEntity : sendTableEntity.getSendClientInfoEntityLists()) {
//					//
//					Channel clientChannel = sendClientInfoEntity.getClientChannel();
//					String ip = sendClientInfoEntity.getIp();
//					int port = sendClientInfoEntity.getPort();
//					sendTableEntity.getNettyServerTCPConfiguration().sendToClient(clientChannel, ip, port, asmMessageAndVsiMessage);
//				}
//			}
//		}
//	}
//	
//	public List<TcpServerTableEntity> getSendTableEntityList() {
//		//
//		return this.sendTableEntityLists;
//	}
//
//	public JTable getCurrentFrameJTableNameUpper() {
//		return currentFrameJTableNameUpper;
//	}
//
//	public void setCurrentFrameJTableNameUpper(JTable currentFrameJTableNameUpper) {
//		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
//	}
//
//	public JTable getCurrentFrame1JTableNameUpper() {
//		return currentFrame1JTableNameUpper;
//	}
//
//	public void setCurrentFrame1JTableNameUpper(JTable currentFrame1JTableNameUpper) {
//		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame2JTableNameUpper() {
//		return currentFrame2JTableNameUpper;
//	}
//
//	public void setCurrentFrame2JTableNameUpper(JTable currentFrame2JTableNameUpper) {
//		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame3JTableNameUpper() {
//		return currentFrame3JTableNameUpper;
//	}
//
//	public void setCurrentFrame3JTableNameUpper(JTable currentFrame3JTableNameUpper) {
//		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame4JTableNameUpper() {
//		return currentFrame4JTableNameUpper;
//	}
//
//	public void setCurrentFrame4JTableNameUpper(JTable currentFrame4JTableNameUpper) {
//		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame5JTableNameUpper() {
//		return currentFrame5JTableNameUpper;
//	}
//
//	public void setCurrentFrame5JTableNameUpper(JTable currentFrame5JTableNameUpper) {
//		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame6JTableNameUpper() {
//		return currentFrame6JTableNameUpper;
//	}
//
//	public void setCurrentFrame6JTableNameUpper(JTable currentFrame6JTableNameUpper) {
//		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame7JTableNameUpper() {
//		return currentFrame7JTableNameUpper;
//	}
//
//	public void setCurrentFrame7JTableNameUpper(JTable currentFrame7JTableNameUpper) {
//		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
//	}
//
//	public JTable getCurrentFrameJTableNameLower() {
//		return currentFrameJTableNameLower;
//	}
//
//	public void setCurrentFrameJTableNameLower(JTable currentFrameJTableNameLower) {
//		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
//	}
//
//	public JTable getCurrentFrame1JTableNameLower() {
//		return currentFrame1JTableNameLower;
//	}
//
//	public void setCurrentFrame1JTableNameLower(JTable currentFrame1JTableNameLower) {
//		this.currentFrame1JTableNameLower = currentFrame1JTableNameLower;
//	}
//
//	public JTable getCurrentFrame2JTableNameLower() {
//		return currentFrame2JTableNameLower;
//	}
//
//	public void setCurrentFrame2JTableNameLower(JTable currentFrame2JTableNameLower) {
//		this.currentFrame2JTableNameLower = currentFrame2JTableNameLower;
//	}
//
//	public JTable getCurrentFrame3JTableNameLower() {
//		return currentFrame3JTableNameLower;
//	}
//
//	public void setCurrentFrame3JTableNameLower(JTable currentFrame3JTableNameLower) {
//		this.currentFrame3JTableNameLower = currentFrame3JTableNameLower;
//	}
//
//	public JTable getCurrentFrame4JTableNameLower() {
//		return currentFrame4JTableNameLower;
//	}
//
//	public void setCurrentFrame4JTableNameLower(JTable currentFrame4JTableNameLower) {
//		this.currentFrame4JTableNameLower = currentFrame4JTableNameLower;
//	}
//
//	public JTable getCurrentFrame5JTableNameLower() {
//		return currentFrame5JTableNameLower;
//	}
//
//	public void setCurrentFrame5JTableNameLower(JTable currentFrame5JTableNameLower) {
//		this.currentFrame5JTableNameLower = currentFrame5JTableNameLower;
//	}
//
//	public JTable getCurrentFrame6JTableNameLower() {
//		return currentFrame6JTableNameLower;
//	}
//
//	public void setCurrentFrame6JTableNameLower(JTable currentFrame6JTableNameLower) {
//		this.currentFrame6JTableNameLower = currentFrame6JTableNameLower;
//	}
//
//	public JTable getCurrentFrame7JTableNameLower() {
//		return currentFrame7JTableNameLower;
//	}
//
//	public void setCurrentFrame7JTableNameLower(JTable currentFrame7JTableNameLower) {
//		this.currentFrame7JTableNameLower = currentFrame7JTableNameLower;
//	}
//
//	public JTable getCurrentFrame8JTableNameUpper() {
//		return currentFrame8JTableNameUpper;
//	}
//
//	public void setCurrentFrame8JTableNameUpper(JTable currentFrame8JTableNameUpper) {
//		this.currentFrame8JTableNameUpper = currentFrame8JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame9JTableNameUpper() {
//		return currentFrame9JTableNameUpper;
//	}
//
//	public void setCurrentFrame9JTableNameUpper(JTable currentFrame9JTableNameUpper) {
//		this.currentFrame9JTableNameUpper = currentFrame9JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame10JTableNameUpper() {
//		return currentFrame10JTableNameUpper;
//	}
//
//	public void setCurrentFrame10JTableNameUpper(JTable currentFrame10JTableNameUpper) {
//		this.currentFrame10JTableNameUpper = currentFrame10JTableNameUpper;
//	}
//
//	public JTable getCurrentFrame8JTableNameLower() {
//		return currentFrame8JTableNameLower;
//	}
//
//	public void setCurrentFrame8JTableNameLower(JTable currentFrame8JTableNameLower) {
//		this.currentFrame8JTableNameLower = currentFrame8JTableNameLower;
//	}
//
//	public JTable getCurrentFrame9JTableNameLower() {
//		return currentFrame9JTableNameLower;
//	}
//
//	public void setCurrentFrame9JTableNameLower(JTable currentFrame9JTableNameLower) {
//		this.currentFrame9JTableNameLower = currentFrame9JTableNameLower;
//	}
//
//	public JTable getCurrentFrame10JTableNameLower() {
//		return currentFrame10JTableNameLower;
//	}
//
//	public void setCurrentFrame10JTableNameLower(JTable currentFrame10JTableNameLower) {
//		this.currentFrame10JTableNameLower = currentFrame10JTableNameLower;
//	}
//
//	public GlobalSlotNumber getGlobalSlotNumber() {
//		return globalSlotNumber;
//	}
//
//	public void setGlobalSlotNumber(GlobalSlotNumber globalSlotNumber) {
//		this.globalSlotNumber = globalSlotNumber;
//	}

}
