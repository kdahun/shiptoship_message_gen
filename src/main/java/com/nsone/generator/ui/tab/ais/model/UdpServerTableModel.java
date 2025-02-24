package com.nsone.generator.ui.tab.ais.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Component;

import com.nsone.generator.system.constant.SystemConstMessage;
import com.nsone.generator.system.netty.send.config.NettyUdpServerConfig;
import com.nsone.generator.system.netty.server.ServerFactory;
import com.nsone.generator.ui.tab.ais.entity.GlobalSlotNumber;
import com.nsone.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;
import com.nsone.generator.ui.tab.ais.entity.TcpServerTableEntity;
import com.nsone.generator.ui.tab.ais.entity.UdpServerTableEntity;
import com.nsone.generator.ui.tab.ais.entity.UdpTargetClientTableEntity;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UdpServerTableModel extends DefaultTableModel {
	//
	private static final long serialVersionUID = -8607268721707122205L;
	private static final String[] columnNames = new String[] { "Desc", "MultiCast", "Open PORT", "Open" };
	private final UdpTargetClientTableModel udpTargetClientTableModel;
	
	private List<UdpServerTableEntity> udpServerTableEntitys = new ArrayList<>();
	
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
	
	UdpServerTableModel(UdpTargetClientTableModel udpTargetClientTableModel
			, ServerFactory serverFactory) {
		//
		super(columnNames, 0);
		this.udpTargetClientTableModel = udpTargetClientTableModel;
		this.serverFactory = serverFactory;
	}
	
	public int getTargetRowCount(int parentSelectedRow) {
		//
		return this.udpServerTableEntitys.get(parentSelectedRow).getUdpTargetClientTableEntityCount();
	}
	
	public void rowSelectedEvent(int parentSelectedRow) {
		//
		this.udpTargetClientTableModel.setRowCount(0); // remove all row
		
		List<UdpTargetClientTableEntity> list = this.udpServerTableEntitys.get(parentSelectedRow).getUdpTargetClientTableEntitys();
		this.udpTargetClientTableModel.setUdpTargetClientTableEntitys(list);
	}
	
	public void addTargetClientRow(int parentSelectedRow, Object[] newRow) {
		//
		UdpTargetClientTableEntity udpTargetClientTableEntity = new UdpTargetClientTableEntity();
		udpTargetClientTableEntity.setDesc(newRow[0].toString());
		udpTargetClientTableEntity.setIp( newRow[1].toString());
		udpTargetClientTableEntity.setPort((int) newRow[2]);
		
		this.udpServerTableEntitys.get(parentSelectedRow).addUdpTargetClientTableEntity(udpTargetClientTableEntity);
		this.udpTargetClientTableModel.addRow(newRow);
	}
	
	public void addRow(Object[] rowData) {
		//
		UdpServerTableEntity udpServerTableEntity = new UdpServerTableEntity();
		udpServerTableEntity.setDesc(rowData[0].toString());
		udpServerTableEntity.setMultiCast(rowData[1].toString());
		udpServerTableEntity.setPort((int) rowData[2]);
		udpServerTableEntity.setStatus((boolean) rowData[3]);

		this.udpServerTableEntitys.add(udpServerTableEntity);
		super.addRow(rowData);
	}
	
	public void removeRow(int parentSelectedRow) {
		//
		this.removeNettyServerUDPConfiguration(this.udpServerTableEntitys.get(parentSelectedRow));
		
		List<UdpTargetClientTableEntity> targetClients = this.udpServerTableEntitys.get(parentSelectedRow).getUdpTargetClientTableEntitys();
		Iterator<UdpTargetClientTableEntity> iterator = targetClients.iterator();

        while (iterator.hasNext()) {
            try {
//                UdpTargetClientTableEntity client = iterator.next();
                iterator.remove();
            } catch (Exception e) {
                e.printStackTrace();
                // 필요하다면 사용자에게 오류 메시지를 전달합니다.
            }
        }
        
        this.udpTargetClientTableModel.setRowCount(0); // remove all row
		
		this.udpServerTableEntitys.remove(parentSelectedRow);
		super.removeRow(parentSelectedRow);
		
	}
	
	public void removeTargetClientRow(int parentSelectedRow, int targetRow) {
		//
		this.udpServerTableEntitys.get(parentSelectedRow).getUdpTargetClientTableEntitys().remove(targetRow);
		this.udpTargetClientTableModel.removeRow(targetRow);
	}
	
	private void makeNettyServerUDPConfiguration(UdpServerTableEntity udpServerTableEntity) {
		//
		NettyUdpServerConfig nettyUdpServerConfig = new NettyUdpServerConfig(udpServerTableEntity, this.globalSlotNumber
				, this.currentFrameJTableNameUpper, this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper
				, this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper, this.currentFrame5JTableNameUpper
				, this.currentFrame6JTableNameUpper, this.currentFrame7JTableNameUpper, this.currentFrame8JTableNameUpper
				, this.currentFrame9JTableNameUpper, this.currentFrame10JTableNameUpper, this.currentFrameJTableNameLower
				, this.currentFrame1JTableNameLower, this.currentFrame2JTableNameLower, this.currentFrame3JTableNameLower
				, this.currentFrame4JTableNameLower, this.currentFrame5JTableNameLower, this.currentFrame6JTableNameLower
				, this.currentFrame7JTableNameLower, this.currentFrame8JTableNameLower, this.currentFrame9JTableNameLower
				, this.currentFrame10JTableNameLower);
		
		this.serverFactory.makeUdpServer(udpServerTableEntity, nettyUdpServerConfig);
	}
	
	private void removeNettyServerUDPConfiguration(UdpServerTableEntity udpServerTableEntity) {
		//
		this.serverFactory.removeUDPServer(udpServerTableEntity);
	}
	
	public void sendAISMessage(String aisMessage, String vsiMessage) {
		//
		for(UdpServerTableEntity sendTableEntity : this.udpServerTableEntitys) {
			//
			if(sendTableEntity.isStatus() && sendTableEntity.getNettyServerUDPConfiguration() != null) {
				//
				StringBuilder sb = new StringBuilder();
				sb.append(aisMessage).append(SystemConstMessage.CRLF);
				sb.append(vsiMessage).append(SystemConstMessage.CRLF);
				
				if(sendTableEntity.getNettyServerUDPConfiguration().getMultiCastInfo() != null) {
					//
					sendTableEntity.getNettyServerUDPConfiguration().sendToClient(sb.toString());
				}else {
					
					for(UdpTargetClientTableEntity udpTargetClientTableEntity : sendTableEntity.getUdpTargetClientTableEntitys()) {
						//
						if(udpTargetClientTableEntity.isAis()) {
							String ip = udpTargetClientTableEntity.getIp();
							int port = Integer.valueOf(udpTargetClientTableEntity.getPort());
							sendTableEntity.getNettyServerUDPConfiguration().sendToClient(ip, port, sb.toString());
						}
					}
				}
			}
		}
	}
	
	public void sendASMMessage(String asmMessageAndVsiMessage) {
		//
		for(UdpServerTableEntity udpServerTableEntity : this.udpServerTableEntitys) {
			//
			if(udpServerTableEntity.isStatus() && udpServerTableEntity.getNettyServerUDPConfiguration() != null ) {
				//
//				udpServerTableEntity.getNettyServerUDPConfiguration().sendToClient(asmMessageAndVsiMessage);
				if(udpServerTableEntity.getNettyServerUDPConfiguration().getMultiCastInfo() != null) {
					//
					udpServerTableEntity.getNettyServerUDPConfiguration().sendToClient(asmMessageAndVsiMessage);
				}else {
					
					for(UdpTargetClientTableEntity udpTargetClientTableEntity : udpServerTableEntity.getUdpTargetClientTableEntitys()) {
						//
						if(udpTargetClientTableEntity.isAis()) {
							String ip = udpTargetClientTableEntity.getIp();
							int port = Integer.valueOf(udpTargetClientTableEntity.getPort());
							udpServerTableEntity.getNettyServerUDPConfiguration().sendToClient(ip, port, asmMessageAndVsiMessage);
						}
					}
				}
			}
		}
	}
	
	
	
	public List<UdpServerTableEntity> getUdpServerTableEntitys() {
		return udpServerTableEntitys;
	}

	public void setUdpServerTableEntitys(List<UdpServerTableEntity> udpServerTableEntitys) {
		this.udpServerTableEntitys = udpServerTableEntitys;
	}

	
	
	public GlobalSlotNumber getGlobalSlotNumber() {
		return globalSlotNumber;
	}

	public void setGlobalSlotNumber(GlobalSlotNumber globalSlotNumber) {
		this.globalSlotNumber = globalSlotNumber;
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

	@Override
	public void setValueAt(Object value, int row, int column) {
		//
		if (column == 0) {
			//
			this.udpServerTableEntitys.get(row).setDesc(value.toString());
			super.setValueAt(value, row, column);
		} else if (column == 1) {
			//
			this.udpServerTableEntitys.get(row).setMultiCast(value.toString());
			super.setValueAt(value, row, column);
		} else if (column == 2) {
			//
			this.udpServerTableEntitys.get(row).setPort(Integer.valueOf(value.toString()) );
			super.setValueAt(value, row, column);
		} else if (column == 3) {
			//
			this.udpServerTableEntitys.get(row).setStatus((boolean) value);
			super.setValueAt(value, row, column);
			if((boolean) value) {
				this.makeNettyServerUDPConfiguration(this.udpServerTableEntitys.get(row));
			}else {
				this.removeNettyServerUDPConfiguration(this.udpServerTableEntitys.get(row));
			}
		}
	}
}
