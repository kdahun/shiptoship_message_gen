package com.all4land.generator.system.netty.send.config;

import java.net.InetSocketAddress;

import javax.swing.JTable;

import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.ui.tab.ais.entity.GlobalSlotNumber;
import com.all4land.generator.ui.tab.ais.entity.TcpServerTableEntity;
import com.all4land.generator.ui.tab.ais.entity.TcpTargetClientInfoEntity;
import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
	//
	private TcpServerTableEntity tcpServerTableEntity;
	private final int RUN_COUNT = 5;
	private final int LIMIT_LENGTH = 9000;
	@SuppressWarnings("FieldMayBeFinal") private StringBuilder receivedBuffer = new StringBuilder();

	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrameJTableNameUpper;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame1JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame2JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame3JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame4JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame5JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame6JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame7JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame8JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame9JTableNameUpper;
	@SuppressWarnings({"unused", "FieldMayBeFinal"})  private JTable currentFrame10JTableNameUpper;

	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrameJTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame1JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame2JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame3JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame4JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame5JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame6JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame7JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame8JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame9JTableNameLower;
	@SuppressWarnings("FieldMayBeFinal") private JTable currentFrame10JTableNameLower;
	
	@SuppressWarnings("FieldMayBeFinal") private GlobalSlotNumber globalSlotNumber;

	TcpServerHandler(NettyTcpServerConfig nettyServerConfig) {
		//
		this.tcpServerTableEntity = nettyServerConfig.getTcpServerTableEntity();
		this.globalSlotNumber = nettyServerConfig.getGlobalSlotNumber();
		
		this.currentFrameJTableNameUpper = nettyServerConfig.getCurrentFrameJTableNameUpper();
		this.currentFrame1JTableNameUpper = nettyServerConfig.getCurrentFrame1JTableNameUpper();
		this.currentFrame2JTableNameUpper = nettyServerConfig.getCurrentFrame2JTableNameUpper();
		this.currentFrame3JTableNameUpper = nettyServerConfig.getCurrentFrame3JTableNameUpper();
		this.currentFrame4JTableNameUpper = nettyServerConfig.getCurrentFrame4JTableNameUpper();
		this.currentFrame5JTableNameUpper = nettyServerConfig.getCurrentFrame5JTableNameUpper();
		this.currentFrame6JTableNameUpper = nettyServerConfig.getCurrentFrame6JTableNameUpper();
		this.currentFrame7JTableNameUpper = nettyServerConfig.getCurrentFrame7JTableNameUpper();
		this.currentFrame8JTableNameUpper = nettyServerConfig.getCurrentFrame8JTableNameUpper();
		this.currentFrame9JTableNameUpper = nettyServerConfig.getCurrentFrame9JTableNameUpper();
		this.currentFrame10JTableNameUpper = nettyServerConfig.getCurrentFrame10JTableNameUpper();
		this.currentFrameJTableNameLower = nettyServerConfig.getCurrentFrameJTableNameLower();
		this.currentFrame1JTableNameLower = nettyServerConfig.getCurrentFrame1JTableNameLower();
		this.currentFrame2JTableNameLower = nettyServerConfig.getCurrentFrame2JTableNameLower();
		this.currentFrame3JTableNameLower = nettyServerConfig.getCurrentFrame3JTableNameLower();
		this.currentFrame4JTableNameLower = nettyServerConfig.getCurrentFrame4JTableNameLower();
		this.currentFrame5JTableNameLower = nettyServerConfig.getCurrentFrame5JTableNameLower();
		this.currentFrame6JTableNameLower = nettyServerConfig.getCurrentFrame6JTableNameLower();
		this.currentFrame7JTableNameLower = nettyServerConfig.getCurrentFrame7JTableNameLower();
		this.currentFrame8JTableNameLower = nettyServerConfig.getCurrentFrame8JTableNameLower();
		this.currentFrame9JTableNameLower = nettyServerConfig.getCurrentFrame9JTableNameLower();
		this.currentFrame10JTableNameLower = nettyServerConfig.getCurrentFrame10JTableNameLower();
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 클라이언트가 연결될 때 호출됩니다.
		String clientIP = ctx.channel().remoteAddress().toString();
		int clientPort = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
		Channel clientChannel = ctx.channel();
		System.out.println("Client connected from IP: " + clientIP);

		TcpTargetClientInfoEntity tcpTargetClientInfoEntity = new TcpTargetClientInfoEntity();
		tcpTargetClientInfoEntity.setIp(clientIP);
		tcpTargetClientInfoEntity.setPort(clientPort);
		tcpTargetClientInfoEntity.setClientChannel(clientChannel);
		tcpTargetClientInfoEntity.setAis(false);
		tcpTargetClientInfoEntity.setAsm(false);
		tcpTargetClientInfoEntity.setTsq(false);
		tcpServerTableEntity.addTcpTargetClientInfoEntity(tcpTargetClientInfoEntity);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// 클라이언트로부터 메시지를 수신할 때 호출됩니다.
		System.out.println("========================Received message from client: " + msg);
		// 이후 작업 수행...
		this.receivedBuffer.append(msg.toString(CharsetUtil.UTF_8));

		for (int i = 0; i < this.RUN_COUNT; i++) {
			//
			try {
				this.process();
			}catch (Exception e) {
				log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
			}
			
		}

		this.limitLengthReceivedMessage();
	}

	private void process() {
		//
		int format450 = this.receivedBuffer.indexOf(SystemConstMessage.StartIndex_for_450);
		int tsrStartIndex = this.receivedBuffer.indexOf(SystemConstMessage.tsrStartIndex);
		int endIndex = this.findEndIndex(tsrStartIndex);

		if (this.isValidResource(format450, tsrStartIndex,  endIndex)) {
			//
			this.processResourceMessage(format450, tsrStartIndex,  endIndex);
		}
	}

	private boolean isValidResource(int format450, int tsrStartIndex, int endIndex) {
		//
		return format450 > -1 && tsrStartIndex > -1 && endIndex > -1 
				&& format450 < tsrStartIndex
				&& tsrStartIndex < endIndex;
	}

	private int findEndIndex(int tsrStartIndex) {
		//
		int firstCRLFAfterVSIIndex = this.receivedBuffer.indexOf(SystemConstMessage.CRLF, tsrStartIndex);
		return firstCRLFAfterVSIIndex != -1 ? firstCRLFAfterVSIIndex : -1;
	}

	private void processResourceMessage(int format450, int tsrStartIndex, int endIndex) {
		//
		for (String split : this.receivedBuffer.substring(format450, endIndex).split(SystemConstMessage.CRLF)) {
			//
			System.out.println("파싱한 데이터: " + split);
			String[] sp_step0 = split.split("\\\\");
			// String sp_450_header = sp_step0[0];
			// String sp_450_body = sp_step0[1];
			String tsr_sp = sp_step0[2];
			
			String[] tsr = tsr_sp.split(",");
			
			
			// String tsr_shore = tsr[3];
			// String tsr_mmsi = tsr[4];
			
			// String field5 = tsr[5];
			// String field6 = tsr[6];
			
			// String tsr_physicalChannel = tsr[7];
			String tsr_tdmaFrame = tsr[8];
			String tsr_slotNumber = tsr[9];
			// String tsr_linkId = tsr[10];
			String tsr_tdmaDelay = tsr[11];
			String tsr_tdmaChannelNumber = tsr[12].split("\\*")[0];
			
			// paint response slot
			if(tsr_slotNumber.equals("")) {
				this.receivedBuffer.delete(format450, endIndex + 2);
				return;
			}
			int int_slotNumber = Integer.parseInt(tsr_slotNumber);
			int now_slotNumber = this.globalSlotNumber.getSlotNumber();
			
			if(tsr_tdmaFrame.equals("")) {
				this.receivedBuffer.delete(format450, endIndex + 2);
				return;
			}
			// int int_tsr_tdmaFrame = Integer.parseInt(tsr_tdmaFrame);
			if(tsr_tdmaDelay.equals("")) {
				this.receivedBuffer.delete(format450, endIndex + 2);
				return;
			}
			int int_tsr_tdmaDelay = Integer.parseInt(tsr_tdmaDelay);
			
			System.out.println("현재 슬롯 : "+now_slotNumber+", response slot : "+ int_slotNumber);
			if(int_slotNumber < now_slotNumber) {
				// 미래것
				this.paintResponseSlot(this.currentFrame1JTableNameUpper, int_slotNumber);
				
			}else {
				this.paintResponseSlot(this.currentFrameJTableNameUpper, int_slotNumber);
				
			}
			
			for(int i = int_tsr_tdmaDelay; i < int_tsr_tdmaDelay + 4; i++) {
				//
				if(i >= 0 && i <= 24) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrameJTableNameLower.repaint();
				}else if(i >= 25 && i <= 49) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame1JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 25, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame1JTableNameLower.repaint();
				}else if(i >= 50 && i <= 74) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame2JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 50, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame2JTableNameLower.repaint();
				}else if(i >= 75 && i <= 99) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame3JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 75, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame3JTableNameLower.repaint();
				}else if(i >= 100 && i <= 124) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame4JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 100, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame4JTableNameLower.repaint();
				}else if(i >= 125 && i <= 149) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame5JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 125, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame5JTableNameLower.repaint();
				}else if(i >= 150 && i <= 174) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame6JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 150, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame6JTableNameLower.repaint();
				}else if(i >= 175 && i <= 199) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame7JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 175, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame7JTableNameLower.repaint();
				}else if(i >= 200 && i <= 224) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame8JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 200, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame8JTableNameLower.repaint();
				}else if(i >= 225 && i <= 249) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame9JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 225, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame9JTableNameLower.repaint();
				}else if(i >= 250 && i <= 255) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame10JTableNameLower.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 250, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame10JTableNameLower.repaint();
				}
			}
		}

		this.receivedBuffer.delete(format450, endIndex + 2);
	}

	private void paintResponseSlot(JTable targetTable, int slotNumber) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) targetTable.getDefaultRenderer(Object.class);
		renderer.paintResourceResponseCell(slotNumber);
		targetTable.repaint();
	}
	
	@SuppressWarnings("unused")
	private void paintResource(JTable targetTable, int tdmaFrame, int tdmaChannel) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) targetTable.getDefaultRenderer(Object.class);
		renderer.paintResources(tdmaFrame, tdmaChannel);
		targetTable.repaint();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 예외가 발생할 때 호출됩니다.
		log.error("Exception [Err_Location] : {}", cause.getStackTrace()[0]);
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 클라이언트가 연결을 종료할 때 호출됩니다.
		String clientIP = ctx.channel().remoteAddress().toString();
		int clientPort = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
		System.out.println("Client disconnected from IP: " + clientIP);
		// 이후 작업 수행...
		this.tcpServerTableEntity.removeTcpTargetClientInfoEntitys(clientIP, clientPort);
	}

	public void setSendTableEntity(TcpServerTableEntity sendTableEntity) {
		//
		this.tcpServerTableEntity = sendTableEntity;
	}

	private void limitLengthReceivedMessage() {
		//
		if (this.receivedBuffer.length() >= LIMIT_LENGTH) {
			//
			log.info("버퍼 길이가 {} 넘었다. 클리어한다.", this.receivedBuffer.length());
			log.info("버퍼 : {} ", this.receivedBuffer);
			this.receivedBuffer.setLength(0);
		}
	}

}