package com.all4land.generator.ui.service;

import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;

import org.springframework.stereotype.Service;

import com.all4land.generator.ais.DestinationIdentification;
import com.all4land.generator.ais.ESIMessageUtil;
import com.all4land.generator.ais.Formatter_61162;
import com.all4land.generator.ais.GroupControl;
import com.all4land.generator.ais.SourceIdentification;
import com.all4land.generator.ais.TagBlock;
import com.all4land.generator.ais.TerrestrialSlotResourceRequest;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.system.netty.send.config.NettyServerTCPConfiguration;
import com.all4land.generator.system.netty.send.config.NettyServerUDPConfiguration;
import com.all4land.generator.ui.tab.ais.entity.GlobalEntityManager;
import com.all4land.generator.ui.tab.ais.entity.TcpServerTableEntity;
import com.all4land.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;
import com.all4land.generator.ui.tab.ais.entity.UdpServerTableEntity;
import com.all4land.generator.ui.tab.ais.model.TcpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpServerTableModel;
import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;
import com.all4land.generator.ui.view.Sample1;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResourceService {
	//
	private int seq = 1;
	
//	private int groupSeq = 1;
	
	private Sample1 mainFrame;
	private TcpServerTableModel tcpServerTableModel;
	private UdpServerTableModel udpServerTableModel;
	private GlobalEntityManager globalEntityManager;

	public void resourceStart(int row, int column, String slotNumber) {
		//
		this.tcpServerTableModel = this.mainFrame.getSendTableModel();
		this.udpServerTableModel = this.mainFrame.getUdpServerTableModel();
		this.globalEntityManager = this.mainFrame.getGlobalEntityManager();

		for (TcpServerTableEntity tcpServerTableEntity : tcpServerTableModel.getTcpServerTableEntitys()) {
			//
			if (tcpServerTableEntity.isStatus() && tcpServerTableEntity.getNettyServerTCPConfiguration() != null) {
				//
				for (TcpTargetClientInfoEntity tcpTargetClientInfoEntity : tcpServerTableEntity
						.getTcpTargetClientInfoEntitys()) {
					if (tcpTargetClientInfoEntity.isTsq()) {
						CompletableFuture.runAsync(() -> this.processTcp(row, column, slotNumber,
								tcpServerTableEntity.getNettyServerTCPConfiguration(), tcpTargetClientInfoEntity));
						seq += 1;
						if(seq >= 1000) {
							seq = 1;
						}
					}
				}
			}
//				process(row, column, slotNumber, true, sendTableEntity);
//			}else if(sendTableEntity.isStatus() && sendTableEntity.getNettyServerUDPConfiguration() != null) {
//				process(row, column, slotNumber, false, sendTableEntity);
//			}else {
//				//
////				log.info("서버 포트가 열려있지않거나, 보낼 클라이언트 정보가 없다.");
//			}
		}

		for (UdpServerTableEntity udpServerTableEntity : udpServerTableModel.getUdpServerTableEntitys()) {
			if (udpServerTableEntity.isStatus() && udpServerTableEntity.getNettyServerUDPConfiguration() != null) {
//				for (UdpTargetClientTableEntity udpTargetClientTableEntity : udpServerTableEntity
//						.getUdpTargetClientTableEntitys()) {
//					if (udpTargetClientTableEntity.isTsq()) {
						CompletableFuture.runAsync(() -> this.processUdp(row, column, slotNumber,
								udpServerTableEntity.getNettyServerUDPConfiguration() ));
						seq += 1;
						if(seq >= 1000) {
							seq = 1;
						}
//					}
//				}
			}
		}
	}

	private void processTcp(int row, int column, String slotNumber,
			NettyServerTCPConfiguration nettyServerTCPConfiguration,
			TcpTargetClientInfoEntity tcpTargetClientInfoEntity) {
		//
		JTable table = this.mainFrame.getCurrentFrameJTableNameLower();
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
		GroupControl groupControl = new GroupControl(1, 2, seq);
		SourceIdentification sourceIdentification = new SourceIdentification(this.mainFrame.getSFIText());
		DestinationIdentification destinationIdentification = new DestinationIdentification("RS0001");
		TagBlock tagBlock = new TagBlock(groupControl, sourceIdentification, destinationIdentification);
		Formatter_61162 Formatter_61162 = new Formatter_61162(tagBlock);
		String Formatter_61162_message = Formatter_61162.getMessage();

		String shoreStationId = globalEntityManager.getUuid();
		long mmsi = Long.MIN_VALUE;
		String mmsiStr = "";

		try {
			mmsi = globalEntityManager.getMmsiEntityLists().get(0).getMmsi();
			mmsiStr = String.valueOf(mmsi);
		} catch (Exception e) {
			log.info("자체 방송용이다");
		}

		String physicalChannelNumber = "1285";
		String linkId = "19";

		TerrestrialSlotResourceRequest req = new TerrestrialSlotResourceRequest(String.valueOf(seq), shoreStationId, mmsiStr,
				physicalChannelNumber, linkId);
		String TSQ_msg = req.getTerrestrialSlotResourceRequest();

		GroupControl groupControlESI = new GroupControl(2, 2, seq);
		SourceIdentification sourceIdentificationESI = new SourceIdentification(this.mainFrame.getSFIText());
		DestinationIdentification destinationIdentificationESI = new DestinationIdentification("RS0001");
		TagBlock tagBlockESI = new TagBlock(groupControlESI, sourceIdentificationESI, destinationIdentificationESI);
		Formatter_61162 Formatter_61162ESI = new Formatter_61162(tagBlockESI);
		String Formatter_61162_messageESI = Formatter_61162ESI.getMessage();

		String tdmaFrame = String.valueOf(renderer.getTdmaFrame(Integer.valueOf(slotNumber)));
		String tdmachannel = "0";
		String totalAccountSlot = "14";
		String firstSlotNumber = slotNumber;

		ESIMessageUtil esi = new ESIMessageUtil(shoreStationId, "0", tdmaFrame, tdmachannel, totalAccountSlot,
				firstSlotNumber, linkId);
		String ESI_msg = esi.getMessage();

		StringBuilder sb = new StringBuilder();
		sb.append("UdPtD \\g:").append("3-1-").append(this.seq).append(",s:").append(this.mainFrame.getSFIText());
		sb.append(",d:RS0001*00\\$VATDB,000000000000,88888,88888,0*00").append(SystemConstMessage.CRLF);
		
		sb.append("UdPbC \\g:").append("3-2-").append(this.seq).append(",s:").append(this.mainFrame.getSFIText());
		sb.append(",d:RS0001*00\\").append(ESI_msg).append(SystemConstMessage.CRLF);
		
		sb.append("UdPbC \\g:").append("3-3-").append(this.seq).append(",s:").append(this.mainFrame.getSFIText());
		sb.append(",d:RS0001*00\\").append(TSQ_msg).append(SystemConstMessage.CRLF);
		
//		sb.append(Formatter_61162_message).append(TSQ_msg).append(SystemConstMessage.CRLF);
//		sb.append(Formatter_61162_messageESI).append(ESI_msg).append(SystemConstMessage.CRLF);
		// for(TcpTargetClientInfoEntity sendClientInfoEntity :
		// sendTableEntity.getSendClientInfoEntityLists()) {
		//
		Channel clientChannel = tcpTargetClientInfoEntity.getClientChannel();
		String ip = tcpTargetClientInfoEntity.getIp();
		int port = tcpTargetClientInfoEntity.getPort();
		this.paintTsqMessageStartSlot(Integer.valueOf(slotNumber));
		System.out.println("send TCP tsq msg : " + sb.toString());
		nettyServerTCPConfiguration.sendToClient(clientChannel, ip, port, sb.toString());
//		}

//		System.out.println(sb);
//		sendTableEntity.getNettyServerTCPConfiguration().sendTcpData(sendTableEntitysb.toString());
//		if(mode) {
//			// tcp
//			for(TcpTargetClientInfoEntity sendClientInfoEntity : sendTableEntity.getSendClientInfoEntityLists()) {
//				//
//				Channel clientChannel = sendClientInfoEntity.getClientChannel();
//				String ip = sendClientInfoEntity.getIp();
//				int port = sendClientInfoEntity.getPort();
//				this.paintTsqMessageStartSlot(Integer.valueOf(slotNumber));
//				System.out.println("send TCP teq msg : "+sb.toString());
//				sendTableEntity.getNettyServerTCPConfiguration().sendToClient(clientChannel, ip, port, sb.toString());
//			}
//		}else {
//			// udp
//			//for(SendClientInfoEntity sendClientInfoEntity : sendTableEntity.getSendClientInfoEntityLists()) {
//				//
////				Channel ch = sendClientInfoEntity.getClientChannel();
////				String ip = sendClientInfoEntity.getIp();
////				int port = sendClientInfoEntity.getPort();
//				this.paintTsqMessageStartSlot(Integer.valueOf(slotNumber));
//				System.out.println("send UDP teq msg : "+sb.toString());
////				sendTableEntity.getNettyServerUDPConfiguration().sendToClient(sendTableEntity.getClientIp(), Integer.valueOf(sendTableEntity.getClientPort()), sb.toString());
////			}
//		}

	}

	private void processUdp(int row, int column, String slotNumber,
			NettyServerUDPConfiguration nettyServerUDPConfiguration
//			, UdpTargetClientTableEntity udpTargetClientTableEntity
			) {
		//
		JTable table = this.mainFrame.getCurrentFrameJTableNameLower();
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
		GroupControl groupControl = new GroupControl(1, 2, seq);
		SourceIdentification sourceIdentification = new SourceIdentification(this.mainFrame.getSFIText());
		DestinationIdentification destinationIdentification = new DestinationIdentification("RS0001");
		TagBlock tagBlock = new TagBlock(groupControl, sourceIdentification, destinationIdentification);
		Formatter_61162 Formatter_61162 = new Formatter_61162(tagBlock);
		String Formatter_61162_message = Formatter_61162.getMessage();

		String shoreStationId = globalEntityManager.getUuid();
		long mmsi = Long.MIN_VALUE;
		String mmsiStr = "";

		try {
			mmsi = globalEntityManager.getMmsiEntityLists().get(0).getMmsi();
			mmsiStr = String.valueOf(mmsi);
		} catch (Exception e) {
			log.info("자체 방송용이다");
		}

		String physicalChannelNumber = "1285";
		String linkId = "19";

		TerrestrialSlotResourceRequest req = new TerrestrialSlotResourceRequest(String.valueOf(seq), shoreStationId, mmsiStr,
				physicalChannelNumber, linkId);

		String TSQ_msg = req.getTerrestrialSlotResourceRequest();

		GroupControl groupControlESI = new GroupControl(2, 2, seq);
		SourceIdentification sourceIdentificationESI = new SourceIdentification(this.mainFrame.getSFIText());
		DestinationIdentification destinationIdentificationESI = new DestinationIdentification("RS0001");
		TagBlock tagBlockESI = new TagBlock(groupControlESI, sourceIdentificationESI, destinationIdentificationESI);
		Formatter_61162 Formatter_61162ESI = new Formatter_61162(tagBlockESI);
		String Formatter_61162_messageESI = Formatter_61162ESI.getMessage();

		String tdmaFrame = String.valueOf(renderer.getTdmaFrame(Integer.valueOf(slotNumber)));
		String tdmachannel = "0";
		String totalAccountSlot = "14";
		String firstSlotNumber = slotNumber;

		ESIMessageUtil esi = new ESIMessageUtil(shoreStationId, "0", tdmaFrame, tdmachannel, totalAccountSlot,
				firstSlotNumber, linkId);
		String ESI_msg = esi.getMessage();

		StringBuilder sb = new StringBuilder();
		sb.append("UdPtD \\g:").append("3-1-").append(this.seq).append(",s:").append(this.mainFrame.getSFIText());
		sb.append(",d:RS0001*00\\$VATDB,000000000000,88888,88888,0*00").append(SystemConstMessage.CRLF);
		
		sb.append("UdPbC \\g:").append("3-2-").append(this.seq).append(",s:").append(this.mainFrame.getSFIText());
		sb.append(",d:RS0001*00\\").append(ESI_msg).append(SystemConstMessage.CRLF);
		
		sb.append("UdPbC \\g:").append("3-3-").append(this.seq).append(",s:").append(this.mainFrame.getSFIText());
		sb.append(",d:RS0001*00\\").append(TSQ_msg).append(SystemConstMessage.CRLF);
		
		
//		sb.append(Formatter_61162_message).append(TSQ_msg).append(SystemConstMessage.CRLF);
//		sb.append(Formatter_61162_messageESI).append(ESI_msg).append(SystemConstMessage.CRLF);
		// for(TcpTargetClientInfoEntity sendClientInfoEntity :
		// sendTableEntity.getSendClientInfoEntityLists()) {
		//
//		Channel clientChannel = tcpTargetClientInfoEntity.getClientChannel();
//		String ip = udpTargetClientTableEntity.getIp();
//		int port = udpTargetClientTableEntity.getPort();
		this.paintTsqMessageStartSlot(Integer.valueOf(slotNumber));
		System.out.println("send UDP tsq msg : " + sb.toString());
		nettyServerUDPConfiguration.sendToClient(sb.toString());
//		}

//		System.out.println(sb);
//		sendTableEntity.getNettyServerTCPConfiguration().sendTcpData(sendTableEntitysb.toString());
//		if(mode) {
//			// tcp
//			for(TcpTargetClientInfoEntity sendClientInfoEntity : sendTableEntity.getSendClientInfoEntityLists()) {
//				//
//				Channel clientChannel = sendClientInfoEntity.getClientChannel();
//				String ip = sendClientInfoEntity.getIp();
//				int port = sendClientInfoEntity.getPort();
//				this.paintTsqMessageStartSlot(Integer.valueOf(slotNumber));
//				System.out.println("send TCP teq msg : "+sb.toString());
//				sendTableEntity.getNettyServerTCPConfiguration().sendToClient(clientChannel, ip, port, sb.toString());
//			}
//		}else {
//			// udp
//			//for(SendClientInfoEntity sendClientInfoEntity : sendTableEntity.getSendClientInfoEntityLists()) {
//				//
////				Channel ch = sendClientInfoEntity.getClientChannel();
////				String ip = sendClientInfoEntity.getIp();
////				int port = sendClientInfoEntity.getPort();
//				this.paintTsqMessageStartSlot(Integer.valueOf(slotNumber));
//				System.out.println("send UDP teq msg : "+sb.toString());
////				sendTableEntity.getNettyServerUDPConfiguration().sendToClient(sendTableEntity.getClientIp(), Integer.valueOf(sendTableEntity.getClientPort()), sb.toString());
////			}
//		}
		
	}

	public Sample1 getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(Sample1 mainFrame) {
		this.mainFrame = mainFrame;
	}

	private void paintTsqMessageStartSlot(int slotNumber) {
		//
		JTable table = this.mainFrame.getCurrentFrameJTableNameLower();
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
		renderer.paintResourceStartCell(slotNumber);
		table.repaint();
	}
}
