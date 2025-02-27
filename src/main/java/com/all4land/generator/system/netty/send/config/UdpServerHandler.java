package com.all4land.generator.system.netty.send.config;

import javax.swing.JTable;

import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.ui.tab.ais.entity.GlobalSlotNumber;
import com.all4land.generator.ui.tab.ais.entity.UdpServerTableEntity;
import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	//
	@SuppressWarnings("unused") private UdpServerTableEntity udpServerTableEntity;
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

	UdpServerHandler(NettyUdpServerConfig nettyUdpServerConfig) {
		this.udpServerTableEntity = nettyUdpServerConfig.getUdpServerTableEntity();
		this.globalSlotNumber = nettyUdpServerConfig.getGlobalSlotNumber();

		this.currentFrameJTableNameUpper = nettyUdpServerConfig.getCurrentFrameJTableNameUpper();
		this.currentFrame1JTableNameUpper = nettyUdpServerConfig.getCurrentFrame1JTableNameUpper();
		this.currentFrame2JTableNameUpper = nettyUdpServerConfig.getCurrentFrame2JTableNameUpper();
		this.currentFrame3JTableNameUpper = nettyUdpServerConfig.getCurrentFrame3JTableNameUpper();
		this.currentFrame4JTableNameUpper = nettyUdpServerConfig.getCurrentFrame4JTableNameUpper();
		this.currentFrame5JTableNameUpper = nettyUdpServerConfig.getCurrentFrame5JTableNameUpper();
		this.currentFrame6JTableNameUpper = nettyUdpServerConfig.getCurrentFrame6JTableNameUpper();
		this.currentFrame7JTableNameUpper = nettyUdpServerConfig.getCurrentFrame7JTableNameUpper();
		this.currentFrame8JTableNameUpper = nettyUdpServerConfig.getCurrentFrame8JTableNameUpper();
		this.currentFrame9JTableNameUpper = nettyUdpServerConfig.getCurrentFrame9JTableNameUpper();
		this.currentFrame10JTableNameUpper = nettyUdpServerConfig.getCurrentFrame10JTableNameUpper();
		this.currentFrameJTableNameLower = nettyUdpServerConfig.getCurrentFrameJTableNameLower();
		this.currentFrame1JTableNameLower = nettyUdpServerConfig.getCurrentFrame1JTableNameLower();
		this.currentFrame2JTableNameLower = nettyUdpServerConfig.getCurrentFrame2JTableNameLower();
		this.currentFrame3JTableNameLower = nettyUdpServerConfig.getCurrentFrame3JTableNameLower();
		this.currentFrame4JTableNameLower = nettyUdpServerConfig.getCurrentFrame4JTableNameLower();
		this.currentFrame5JTableNameLower = nettyUdpServerConfig.getCurrentFrame5JTableNameLower();
		this.currentFrame6JTableNameLower = nettyUdpServerConfig.getCurrentFrame6JTableNameLower();
		this.currentFrame7JTableNameLower = nettyUdpServerConfig.getCurrentFrame7JTableNameLower();
		this.currentFrame8JTableNameLower = nettyUdpServerConfig.getCurrentFrame8JTableNameLower();
		this.currentFrame9JTableNameLower = nettyUdpServerConfig.getCurrentFrame9JTableNameLower();
		this.currentFrame10JTableNameLower = nettyUdpServerConfig.getCurrentFrame10JTableNameLower();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		//
		// 자신의 주소를 가져옵니다. 예를 들어, localhost의 주소는 InetAddress.getLocalHost()를 사용할 수 있습니다.
	    // InetAddress localIp = InetAddress.getLocalHost();
		// 패킷의 송신자 정보를 확인합니다.
	    // InetSocketAddress senderAddress = packet.sender();
	    // InetAddress senderIp = senderAddress.getAddress();
	    // String serverPort = String.parseInt(senderAddress.getPort());
//	    if (senderIp.equals(localIp)) { // if (senderIp.equals(localIp) || senderIp.isLoopbackAddress()) {
//	        // 자신의 주소인 경우 처리를 건너뜁니다.
//	        return;
//	    }
		String receivedMessage = packet.content().toString(CharsetUtil.UTF_8);
		System.out.println("========================Received message from client: " + receivedMessage);

		System.out.println(">> packet sender :: "+ packet.sender());
		this.receivedBuffer.append(receivedMessage);

		for (int i = 0; i < this.RUN_COUNT; i++) {
			try {
				this.process();
			} catch (Exception e) {
				log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
			}
		}

		this.limitLengthReceivedMessage();
	}

	private void process() {
		int format450 = this.receivedBuffer.indexOf(SystemConstMessage.StartIndex_for_450);
		int tsrStartIndex = this.receivedBuffer.indexOf(SystemConstMessage.tsrStartIndex);
		int endIndex = this.findEndIndex(tsrStartIndex);

		if (this.isValidResource(format450, tsrStartIndex, endIndex)) {
			this.processResourceMessage(format450, tsrStartIndex, endIndex);
		}
	}

	private boolean isValidResource(int format450, int tsrStartIndex, int endIndex) {
		return format450 > -1 && tsrStartIndex > -1 && endIndex > -1 && format450 < tsrStartIndex
				&& tsrStartIndex < endIndex;
	}

	private int findEndIndex(int tsrStartIndex) {
		int firstCRLFAfterVSIIndex = this.receivedBuffer.indexOf(SystemConstMessage.CRLF, tsrStartIndex);
		return firstCRLFAfterVSIIndex != -1 ? firstCRLFAfterVSIIndex : -1;
	}

	private void processResourceMessage(int format450, int tsrStartIndex, int endIndex) {
		for (String split : this.receivedBuffer.substring(format450, endIndex).split(SystemConstMessage.CRLF)) {
			System.out.println("파싱한 데이터: " + split);
			String[] sp_step0 = split.split("\\\\");
			// String sp_450_header = sp_step0[0];
			// String sp_450_body = sp_step0[1];
			String tsr_sp = sp_step0[2];

			String[] tsr = tsr_sp.split(",");
			// String tsr_shore = tsr[1];
			// String tsr_mmsi = tsr[2];
			// String tsr_physicalChannel = tsr[3];
			String tsr_tdmaFrame = tsr[4];
			String tsr_slotNumber = tsr[5];
			// String tsr_linkId = tsr[6];
			String tsr_tdmaDelay = tsr[7];
			String tsr_tdmaChannelNumber = tsr[8].split("\\*")[0];

			if (tsr_slotNumber.equals("")) {
				this.receivedBuffer.delete(format450, endIndex + 2);
				return;
			}
			int int_slotNumber = Integer.parseInt(tsr_slotNumber);
			int now_slotNumber = this.globalSlotNumber.getSlotNumber();

			if (tsr_tdmaFrame.equals("")) {
				this.receivedBuffer.delete(format450, endIndex + 2);
				return;
			}
			// int int_tsr_tdmaFrame = Integer.parseInt(tsr_tdmaFrame);
			if (tsr_tdmaDelay.equals("")) {
				this.receivedBuffer.delete(format450, endIndex + 2);
				return;
			}
			int int_tsr_tdmaDelay = Integer.parseInt(tsr_tdmaDelay);

			System.out.println("현재 슬롯 : " + now_slotNumber + ", response slot : " + int_slotNumber);
			if (int_slotNumber < now_slotNumber) {
				this.paintResponseSlot(this.currentFrame1JTableNameUpper, int_slotNumber);
			} else {
				this.paintResponseSlot(this.currentFrameJTableNameUpper, int_slotNumber);
			}

			for (int i = int_tsr_tdmaDelay; i < int_tsr_tdmaDelay + 4; i++) {
				if (i >= 0 && i <= 24) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrameJTableNameLower.repaint();
				} else if (i >= 25 && i <= 49) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame1JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 25, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame1JTableNameLower.repaint();
				} else if (i >= 50 && i <= 74) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame2JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 50, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame2JTableNameLower.repaint();
				} else if (i >= 75 && i <= 99) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame3JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 75, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame3JTableNameLower.repaint();
				} else if (i >= 100 && i <= 124) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame4JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 100, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame4JTableNameLower.repaint();
				} else if (i >= 125 && i <= 149) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame5JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 125, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame5JTableNameLower.repaint();
				} else if (i >= 150 && i <= 174) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame6JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 150, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame6JTableNameLower.repaint();
				} else if (i >= 175 && i <= 199) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame7JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 175, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame7JTableNameLower.repaint();
				} else if (i >= 200 && i <= 224) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame8JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 200, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame8JTableNameLower.repaint();
				} else if (i >= 225 && i <= 249) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame9JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 225, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame9JTableNameLower.repaint();
				} else if (i >= 250 && i <= 255) {
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrame10JTableNameLower
							.getDefaultRenderer(Object.class);
					renderer.paintResources(i - 250, Integer.parseInt(tsr_tdmaChannelNumber));
					this.currentFrame10JTableNameLower.repaint();
				}
			}
		}

		this.receivedBuffer.delete(format450, endIndex + 2);
	}

	private void paintResponseSlot(JTable targetTable, int slotNumber) {
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) targetTable.getDefaultRenderer(Object.class);
		renderer.paintResourceResponseCell(slotNumber);
		targetTable.repaint();
	}

	@SuppressWarnings("unused")
	private void paintResource(JTable targetTable, int tdmaFrame, int tdmaChannel) {
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) targetTable.getDefaultRenderer(Object.class);
		renderer.paintResources(tdmaFrame, tdmaChannel);
		targetTable.repaint();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("Exception [Err_Location] : {}", cause.getStackTrace()[0]);
		ctx.close();
	}

	public void setSendTableEntity(UdpServerTableEntity udpServerTableEntity) {
		this.udpServerTableEntity = udpServerTableEntity;
	}

	private void limitLengthReceivedMessage() {
		if (this.receivedBuffer.length() >= LIMIT_LENGTH) {
			log.info("버퍼 길이가 {} 넘었다. 클리어한다.", this.receivedBuffer.length());
			log.info("버퍼 : {} ", this.receivedBuffer);
			this.receivedBuffer.setLength(0);
		}
	}
	
//	private void checkInactiveClients() {
//        long currentTime = System.currentTimeMillis();
//        clients.entrySet().removeIf(entry -> {
//            boolean isInactive = (currentTime - entry.getValue().getLastActiveTime()) > INACTIVE_TIMEOUT;
//            if (isInactive) {
//                log.info("Client disconnected due to inactivity: " + entry.getKey());
//            }
//            return isInactive;
//        });
//    }
}
