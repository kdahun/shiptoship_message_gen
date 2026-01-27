package com.all4land.generator.ui.tab.ais.entity;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.all4land.generator.ais.DestinationIdentification;
import com.all4land.generator.ais.Formatter_61162;
import com.all4land.generator.ais.GroupControl;
import com.all4land.generator.ais.SourceIdentification;
import com.all4land.generator.ais.TagBlock;
import com.all4land.generator.ais.VSIMessageUtil;
import com.all4land.generator.system.component.TimeMapRangeCompnents;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.ui.tab.ais.entity.event.change.MmsiEntityChangeStartTimeEvent;
import com.all4land.generator.ui.tab.ais.entity.event.change.MmsiEntitySlotTimeChangeEvent;
import com.all4land.generator.ui.tab.ais.entity.event.change.SlotTimeOutChangeEvent;
import com.all4land.generator.ui.tab.ais.model.TcpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpServerTableModel;
import com.all4land.generator.ui.util.TimeString;
import com.all4land.generator.util.RandomGenerator;

import dk.dma.ais.sentence.Vdm;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("uiMmsiEntity")
public class MmsiEntity {
	//
	private final QuartzCoreService quartzCoreService;
	private final TimeMapRangeCompnents timeMapRangeCompnents;
	private final TcpServerTableModel sendTableModel;
	private final UdpServerTableModel udpServerTableModel;

	private boolean chk;		// setChk(true) 호출 시 AIS메시지 생성 프로세스 시작

	@Getter	@Setter
	private long mmsi;			// MMSI 고유값

	private boolean asm;		// ASM 메시지 생성 여부
	private boolean vde;		// VDE 메시지 생성 여부

	private int nIndex = 0;		// n 의 시작점
	private Boolean targetChannel = true; // true : A channel, false : B Channel

	private int shootCount = -1;// shootCount가 1이 되면 nIndex 증가 및 targetChannel 변경

	private int speed;			// RI 보고간격
	private double RR;			// AIS 메시지가 전송되는 빈도(Report Rate) calculateReportRate
	private int[] nArray;		// RR을 기반으로 생성되는 배열, NS 계산에 사용 nArray(double RR)
	private int NI;				// AIS 메시지가 전송되는 빈도(Report Rate)에 따른 슬롯 번호의 증가량(Nominal Increment) calculateNominalIncrement
	private int NS;				// AIS 메시지가 전송될 슬롯 번호(Nominal Slot) calculatedNominalSlot
	private int NSS;			// AIS 메시지가 전송될 초기 슬롯 번호(Nominal Slot Start) getStartSlotNumber
	private int[] SI;			// AIS 메시지가 전송될 슬롯 번호의 범위(Selection Interval) calculateSelectionInterval
	
	private int positionsCnt = 0;	// this MmsiEntity의 할당 슬롯 수
	private Map<Integer, double[]> positions = new HashMap<>();		// 슬롯번호, 해당 슬롯의 할당 시간대
	
	private int format450_AIS = 0;	// 450 포맷 AIS메시지 시퀀스
	private int format450_ASM = 0;	// 450 포맷 ASM메시지 시퀀스
	private int aisMessageSequence = 0;	// AIS 메시지 시퀀스
	private int asmMessageSequence = 0;	// ASM 메시지 시퀀스

	private LocalDateTime startTime; // AIS 시작시간
	private Vdm message1;	// AIS 메시지
	
	private List<String> asmMessageList;	// ASM 메시지 리스트

	private int slotTimeOut_default;		// 테스트용 slotTimeOut testInit에서 초기화
	private int slotTimeOut = RandomGenerator.generateRandomIntFromTo(0, 7);	// AIS 메시지 타임아웃 시작 시간

	private AsmEntity asmEntity;	// ASM 메시지 관리 Entity
	private VdeEntity vdeEntity;	// VDE 메시지 관리 Entity

	// AIS 점유 슬롯 리스트
	private List<com.all4land.generator.ui.tab.ais.entity.TargetSlotEntity> targetSlotEntity = new ArrayList<>();

	// slot time out 시작 시간
	private LocalDateTime slotTimeOutTime;

	// AIS Job
	private JobDetail startTimeJob;

	// slot time out Job
	private JobDetail slotTimeOutJob;
	private final Scheduler scheduler;

	// log console
	private JTextArea aisTabjTextAreaName;

	private GlobalEntityManager globalEntityManager;

	private JTable currentFrameJTableNameUpper;
	private JTable currentFrame1JTableNameUpper;
	private JTable currentFrame2JTableNameUpper;
	private JTable currentFrame3JTableNameUpper;
	private JTable currentFrame4JTableNameUpper;
	private JTable currentFrame5JTableNameUpper;
	private JTable currentFrame6JTableNameUpper;
	private JTable currentFrame7JTableNameUpper;

	private JTable currentFrameJTableNameLower;
	private JTable currentFrame1JTableNameLower;
	private JTable currentFrame2JTableNameLower;
	private JTable currentFrame3JTableNameLower;
	private JTable currentFrame4JTableNameLower;
	private JTable currentFrame5JTableNameLower;
	private JTable currentFrame6JTableNameLower;
	private JTable currentFrame7JTableNameLower;

	private final ApplicationEventPublisher eventPublisher;
	
	private JTextField jTextFieldSFI;

	public MmsiEntity(ApplicationEventPublisher eventPublisher, Scheduler scheduler,
			QuartzCoreService quartzCoreService, TimeMapRangeCompnents timeMapRangeCompnents
			, TcpServerTableModel sendTableModel, UdpServerTableModel udpServerTableModel) {
		//
		this.sendTableModel = sendTableModel;
		this.udpServerTableModel= udpServerTableModel;
		this.eventPublisher = eventPublisher;
		this.scheduler = scheduler;
		this.quartzCoreService = quartzCoreService;
		this.timeMapRangeCompnents = timeMapRangeCompnents;

		this.NSS = -1;
		
		AsmEntity asmEntity = new AsmEntity(eventPublisher);
		setAsmEntity(asmEntity);
	}

	public void plusPositionCnt() {
		//
		this.positionsCnt += 1;
	}
	
	public int getPositionsCnt() {
		return positionsCnt;
	}

	public void setPositionsCnt(int positionsCnt) {
		this.positionsCnt = positionsCnt;
	}

	public Map<Integer, double[]> getPositions() {
		return positions;
	}

	public void setPositions(Map<Integer, double[]> positions) {
		this.positions = positions;
	}
	
	public int getSlotTimeOut_default() {
		return slotTimeOut_default;
	}

	public void testInit(int speed, int slotTimeout, Map<Integer, double[]> targetPosition) {
		//
		this.speed = speed;
		this.slotTimeOut = slotTimeout;
		this.slotTimeOut_default = this.slotTimeOut;
		
		double[] latitudes = new double[targetPosition.size()];
        double[] longitudes = new double[targetPosition.size()];

        // Extract the latitudes and longitudes
        for (int i = 0; i < targetPosition.size(); i++) {
            double[] position = targetPosition.get(i);
            latitudes[i] = position[0];
            longitudes[i] = position[1];
        }
        
        //=======================================
        
        if(this.speed == 180) {
        	this.positions = targetPosition;
        	System.out.println("mmsi :" + this.mmsi + ", speed : "+ this.speed);
        	
//        	try {
//				Thread.sleep(200);
//			}catch (Exception e) {
//				// TODO: handle exception
//			}
        	
        }else {
        	int numPoints = (((8) * 60) / this.speed) + 1;
//        	int numPoints = (((this.slotTimeOut +1) * 60) / this.speed) + 1;  // 7 minutes * 60 seconds / 2 seconds + 1
            
            double[] timeSeries = new double[numPoints];
            for (int i = 0; i < numPoints; i++) {
                timeSeries[i] = 4.0 * i / (numPoints - 1);  // 4 intervals between 5 points
            }

            // Interpolate latitude and longitude
            double[] latInterp = interpolate(timeSeries, latitudes);
            double[] lonInterp = interpolate(timeSeries, longitudes);
            
            Map<Integer, double[]> positions = new HashMap<>();
//            for (int i = 0; i < numPoints; i++) {
//                positions.put(i, new double[]{latInterp[i], lonInterp[i]});
//            }
            
            for (int i = 0; i < numPoints; i++) {
                double[] position = new double[3];
                position[0] = latInterp[i];
                position[1] = lonInterp[i];

                // Calculate COG if not the last point
                if (i < numPoints - 1) {
                    double cog = calculateCOG(latInterp[i], lonInterp[i], latInterp[i + 1], lonInterp[i + 1]);
                    position[2] = cog;
                } else {
                    position[2] = 0; // Last point has no COG
                }

                positions.put(i, position);
            }
            
            // Calculate COG for each pair of points
//            for (int i = 0; i < numPoints - 1; i++) {  // Ensure we do not go out of bounds
//                double[] currPos = positions.get(i);
//                double[] nextPos = positions.get(i + 1);
//                double cog = calculateCOG(currPos[0], currPos[1], nextPos[0], nextPos[1]);
//                System.out.printf("Key: %d, Latitude: %.6f, Longitude: %.6f, COG: %.2f%n",
//                        i, currPos[0], currPos[1], cog);
//            }
            
            this.positions = positions;
            
            System.out.println("mmsi :" + this.mmsi + ", speed : "+ this.speed);
            
//            for (Map.Entry<Integer, double[]> entry : positions.entrySet()) {
//                System.out.printf("Key: %d, Latitude: %.6f, Longitude: %.6f, COG: %f%n",
//                        entry.getKey(), entry.getValue()[0], entry.getValue()[1], entry.getValue()[2]);
////            	System.out.printf("%.6f %.6f%n", entry.getValue()[0], entry.getValue()[1] );
//            }
            
//            System.out.println("==============================");
            
            
//            try {
//				Thread.sleep(200);
//			}catch (Exception e) {
//				// TODO: handle exception
//			}
        }
        
        
	}
	
	public int getAsmMessageSequence() {
		return asmMessageSequence;
	}



	public void setAsmMessageSequence(int asmMessageSequence) {
		this.asmMessageSequence = asmMessageSequence;
		if(this.asmMessageSequence > 9) {
			//
			this.asmMessageSequence = 0;
		}
	}



	public int getAisMessageSequence() {
		return aisMessageSequence;
	}



	public void setAisMessageSequence(int aisMessageSequence) {
		this.aisMessageSequence = aisMessageSequence;
		if(this.aisMessageSequence > 9) {
			this.aisMessageSequence = 0;
		}
	}



	public int getNS() {
		return NS;
	}

	public void setNS(int NS) {
		this.NS = NS;
	}

	public int getNSS() {
		return this.NSS;
	}

	public void setNSS(int NSS) {
		this.NSS = NSS;
	}

	public Boolean getTargetChannel() {
		return this.targetChannel;
	}

	public void setTargetChannel(Boolean targetChannel) {
		this.targetChannel = targetChannel;
	}

	public int[] getSI() {
		return this.SI;
	}

	public void setSI(int[] SI) {
		this.SI = SI;
	}

	public List<String> getAsmMessageList() {
		return asmMessageList;
	}

	public int getShootCount() {
		return this.shootCount;
	}

	public void setShootCount(int shootCount) {
		this.shootCount = shootCount;

		if (this.shootCount >= 1) {
			//
			this.shootCount = -1;
			this.setnIndex(this.getnIndex() + 1);
		}

		if (this.getTargetChannel()) {
			//
			this.setTargetChannel(false);
//			log.info("111 {}", this.getTargetChannel());
		} else {
			this.setTargetChannel(true);
//			log.info("222 {}", this.getTargetChannel());
		}

	}

	public void clearShootCount(int shootCount) {
		this.shootCount = shootCount;

	}

	public GlobalEntityManager getGlobalEntityManager() {
		return this.globalEntityManager;
	}

	public void setGlobalEntityManager(GlobalEntityManager globalEntityManager) {
		this.globalEntityManager = globalEntityManager;
	}

	public JobDetail getSlotTimeOutJob() {
		return slotTimeOutJob;
	}

	public void setSlotTimeOutJob(JobDetail slotTimeOutJob) {
		this.slotTimeOutJob = slotTimeOutJob;
	}

	public VdeEntity getVdeEntity() {
		return vdeEntity;
	}

	public void setVdeEntity(VdeEntity vdeEntity) {
		this.vdeEntity = vdeEntity;
	}

	public int getnIndex() {
		return this.nIndex;
	}

	public void setnIndex(int nIndex) {
		//
		if (nIndex > this.nArray.length - 1) {
			this.nIndex = 0;
		} else {
			this.nIndex = nIndex;
		}

	}

	public AsmEntity getAsmEntity() {
		return this.asmEntity;
	}

	public void setAsmEntity(AsmEntity asmEntity) {
		this.asmEntity = asmEntity;
	}

	public List<com.all4land.generator.ui.tab.ais.entity.TargetSlotEntity> getTargetSlotEntity() {
		return this.targetSlotEntity;
	}

	public void setTargetSlotEntity(List<com.all4land.generator.ui.tab.ais.entity.TargetSlotEntity> targetSlotEntity) {
		this.targetSlotEntity = targetSlotEntity;
	}

	public void addTargetSlotEntity(com.all4land.generator.ui.tab.ais.entity.TargetSlotEntity targetSlotEntity) {
		//
		this.targetSlotEntity.add(targetSlotEntity);
	}

	public void clearTargetSlotEntity() {
		//
		this.targetSlotEntity.clear();
	}

	public LocalDateTime getSlotTimeOutTime() {
		return slotTimeOutTime;
	}

	/**
	 * [SLOT_TIME_FLOW]-1
	 * SlotTimeOut의 시간이 변경되면 이벤트 발행
	 * @param slotTimeOutTime 슬롯 타임아웃 시간
	 */
	public void setSlotTimeOutTime(LocalDateTime slotTimeOutTime) {
		//
		this.slotTimeOutTime = slotTimeOutTime;
		if (slotTimeOutTime != null) {
			MmsiEntitySlotTimeChangeEvent event = new MmsiEntitySlotTimeChangeEvent(this, "slotTimeOutTime",
					this.slotTimeOutTime, slotTimeOutTime, this, this.globalEntityManager);

			eventPublisher.publishEvent(event);
		}
	}

	public int getSlotTimeOut() {
		return slotTimeOut;
	}

	/**
	 * [MMSI_AIS_FLOW]-2-1
	 * MmsiEntity생성 이벤트에서 add_180은 3, 나머지는 7로 초기화 후 
	 * 해당 SlotTimeOutChangeEvent 이벤트 발행
	//  * 추측] 한 플로우가 끝나면 -1이 되는걸로 봐서 몇 프레임까지 진행할 것인지에 대한 변수가 아닐까
	 * MmsiEntitySlotTimeChangeQuartz Job이 실행되면 slotTimeOut 값에 따라 0이하일땐 0~7랜덤 0보다 클경우 slotTimeOut - 1로 변경. 다시 이 이벤트 발행
	 */
	public void setSlotTimeOut(int slotTimeOut) {
		//
		this.slotTimeOut = slotTimeOut;
		if (slotTimeOut > -1) {
			SlotTimeOutChangeEvent event = new SlotTimeOutChangeEvent(this, 'A', this, this.currentFrameJTableNameUpper,
					this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper,
					this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper,
					this.currentFrame5JTableNameUpper, this.currentFrame6JTableNameUpper,
					this.currentFrame7JTableNameUpper);

			eventPublisher.publishEvent(event);
		}

	}

	public JTextArea getAisTabjTextAreaName() {
		return this.aisTabjTextAreaName;
	}

	public void setAisTabjTextAreaName(JTextArea aisTabjTextAreaName) {
		this.aisTabjTextAreaName = aisTabjTextAreaName;
	}

	public boolean isChk() {
		return this.chk;
	}

	/**
	 * [MMSI_AIS_FLOW]-2-2
	 * QuartzCoreService Scheduler에 Job등록을 위한 함수, 시작 시간 설정 후 RR, NI 초기화
	 * To [MMSI_AIS_FLOW]-3 MmsiEntity.setStartTime
	 */
	public void setChk(boolean chk) {
		this.chk = chk;
		if (!this.chk) {
			//
			try {
				this.quartzCoreService.removeStartTimeTrigger(this);
				this.quartzCoreService.removeSlotTimeOutTimeTrigger(this);

			} catch (SchedulerException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.clearTargetSlotEntity();
			this.message1 = null;
			this.slotTimeOutTime = null;
			this.slotTimeOutJob = null;

			this.targetSlotEntity.clear();

		} else {
			//
//			this.setCalculateBasic();
			this.setStartTime(RandomGenerator.generateRandomLocalDateTimeFormToAddMillisec(3500, 10502));
//			int randomDelay = RandomGenerator.generateRandomIntFromTo(3, 10);
//			this.setStartTime(LocalDateTime.now().plusSeconds(randomDelay));
			this.resetRRAndNI();
		}
	}

	/**
	 * MmsiEntity의 시작시간 슬롯 번호 검색
	 * @return startTime에 해당하는 슬롯번호
	 */
	public int getStartSlotNumber() {
		//
		return this.timeMapRangeCompnents
				.findSlotNumber(this.getStartTime().format(SystemConstMessage.formatterForStartIndex));
	}

	// public long getMmsi() {
	// 	return this.mmsi;
	// }

	// public void setMmsi(long mmsi) {
	// 	this.mmsi = mmsi;
	// }

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		if (this.speed == 180) {
			//
			this.slotTimeOut = 3; // keep flag 같은 의미다.
		}
	}

	public void resetRRAndNI() {
		//
		this.RR = this.calculateReportRate(this.getSpeed()); // 보고율(Rr) 계산
		this.nArray = this.nArray(RR);
		this.NI = this.calculateNominalIncrement(RR); // 명목적 증분(NI) 계산
		if (this.getNSS() <= -1) {
			this.setNSS(this.getStartSlotNumber());
		}
	}

	public double getRR() {
		return this.RR;
	}

	public void setRR(double RR) {
		this.RR = RR;
	}

	public int[] getnArray() {
		return this.nArray;
	}

	public void setnArray(int[] nArray) {
		this.nArray = nArray;
	}

	public int getNI() {
		return this.NI;
	}

	public void setNI(int NI) {
		this.NI = NI;
	}

	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	/**
	 * [MMSI_AIS_FLOW]-3
	 * 시작 시간 변경 이벤트 생성 및 발행
	 * To [MMSI_AIS_FLOW]-4 MmsiEntityChangeStartTimeListener.onMyPojoChange(@EventListener)
	 */
	public void setStartTime(LocalDateTime startTime) {
		//
		this.startTime = startTime;

		MmsiEntityChangeStartTimeEvent event = new MmsiEntityChangeStartTimeEvent(this, "startTime", this.startTime,
				startTime, this);
		eventPublisher.publishEvent(event);
	}

	public void setSelectionInterval() {
		//
		if (this.getSpeed() <= 30) {
			//
			this.setNS(this.calculateNominalSlotDualChannel());
			this.setSI(calculateSelectionInterval(this.getNS(), this.getNI()));
		} else {
			//
			this.setSI(calculateSelectionInterval(this.getStartSlotNumber(), 40));
		}
	}

	public int calculateNominalSlotDualChannel() {
		//
		if (this.getShootCount() == -1) {
			return (int) Math.min(this.getNSS() + (nArray[this.getnIndex()] * 2 * this.getNI()), 2249);
		} else {
			return (int) Math.min((this.getNSS() + this.getNI()) + (nArray[this.getnIndex()] * 2 * this.getNI()), 2249);
		}

	}

	public Vdm getMessage1() {
		return message1;
	}

	/**
	 * [MMSI_AIS_FLOW]-6-1-2
	 * AIS 메시지 생성 및 UDP 전송
	 * @param message1 AIS 메시지
	 * @param slotNumber 슬롯 번호
	 */
	public void setMessage1(Vdm message1, int slotNumber) {
		this.message1 = message1;
		if (this.message1 != null) {
			//
			if(this.format450_AIS >= 99) {
				//
				this.format450_AIS = 0; 
			}
			GroupControl groupControl = new GroupControl(1,2,this.format450_AIS);
			SourceIdentification sourceIdentification = new SourceIdentification(this.jTextFieldSFI.getText());
			DestinationIdentification destinationIdentification = new DestinationIdentification("RS0001");
			TagBlock tagBlock = new TagBlock(groupControl, sourceIdentification, destinationIdentification);
			Formatter_61162 Formatter_61162 = new Formatter_61162(tagBlock);
			String Formatter_61162_message = Formatter_61162.getMessage();
			
			String vsiMessage = this.makeSendVsiMessage(slotNumber, this.getAisMessageSequence(), this.startTime);
			String aisMessage = this.message1.getEncoded();
			
			StringBuilder sbTime = new StringBuilder(SystemConstMessage.CRLF)
					.append(TimeString.getLogDisplay(this.startTime));
			sbTime.append(" MMSI : ").append(this.mmsi);
			sbTime.append(" ->");
			sbTime.append(Formatter_61162_message);
			sbTime.append(aisMessage);
			
			//VSI
			GroupControl groupControlVSI = new GroupControl(2,2,this.format450_AIS);
			SourceIdentification sourceIdentificationVSI = new SourceIdentification(this.jTextFieldSFI.getText());
			DestinationIdentification destinationIdentificationVSI = new DestinationIdentification("RS0001");
			TagBlock tagBlockVSI = new TagBlock(groupControlVSI, sourceIdentificationVSI, destinationIdentificationVSI);
			Formatter_61162 Formatter_61162_VSI = new Formatter_61162(tagBlockVSI);
			String Formatter_61162_VSI_message = Formatter_61162_VSI.getMessage();
			
			sbTime.append(SystemConstMessage.CRLF).append(TimeString.getLogDisplay(this.startTime));
			sbTime.append(" MMSI : ").append(this.mmsi);
			sbTime.append(" ->");
			sbTime.append(Formatter_61162_VSI_message);
			sbTime.append(vsiMessage);
			
			this.format450_AIS = this.format450_AIS + 1;
			
			if(this.mmsi == 868819726L) {
				//
				System.out.println("???");
				System.out.println(sbTime.toString());
			}
			
			CompletableFuture.runAsync(() -> this.printConsoleLog(sbTime.toString()));

			// TCP로 전송하는 메시지... 확인 필요
			CompletableFuture.runAsync(() -> this.sendTableModel.sendAISMessage(
					Formatter_61162_message+aisMessage, Formatter_61162_VSI_message+vsiMessage));
			
			CompletableFuture.runAsync(() -> this.udpServerTableModel.sendAISMessage(
					Formatter_61162_message+aisMessage, Formatter_61162_VSI_message+vsiMessage));
		}
	}
		
	public void setAsmMessageList(List<String> asmMessageList, String slotNumber) {
		//
		String vsiMessage = this.makeSendVsiMessage(Integer.parseInt(slotNumber), this.getAsmMessageSequence(), this.asmEntity.getStartTime());
		
		this.asmMessageList = asmMessageList;
		StringBuilder sb = new StringBuilder();
		StringBuilder sbForSend = new StringBuilder();
		if (this.asmMessageList != null) {
			//
			if(this.format450_ASM >= 99) {
				//
				this.format450_ASM = 0;
			}
			for(int i = 0 ; i < this.asmMessageList.size(); i++) {
				//for(String asmMessage : asmMessageList) {
					//
					GroupControl groupControl = new GroupControl(i+1, this.asmMessageList.size()+1,this.format450_ASM);
					SourceIdentification sourceIdentification = new SourceIdentification(this.jTextFieldSFI.getText());
					DestinationIdentification destinationIdentification = new DestinationIdentification("RS0001");
					TagBlock tagBlock = new TagBlock(groupControl, sourceIdentification, destinationIdentification);
					Formatter_61162 Formatter_61162 = new Formatter_61162(tagBlock);
					String Formatter_61162_message = Formatter_61162.getMessage();
					
					//String vsiMessage = this.makeSendVsiMessage(slotNumber, this.getAisMessageSequence(), this.startTime);
					//String aisMessage = this.message1.getEncoded();
					
					StringBuilder sbTime = new StringBuilder(SystemConstMessage.CRLF)
							.append(TimeString.getLogDisplay(this.asmEntity.getStartTime()));
					sbTime.append(" MMSI : ").append(this.mmsi);
					sbTime.append(" ->");
					sbTime.append(Formatter_61162_message);
					sbTime.append(this.asmMessageList.get(i));
					sb.append(sbTime);
					
					sbForSend.append(Formatter_61162_message+this.asmMessageList.get(i)).append(SystemConstMessage.CRLF);
//					StringBuilder sbTime = new StringBuilder(SystemConstMessage.CRLF)
//							.append(TimeString.getLogDisplay(this.asmEntity.getStartTime()));
//					sbTime.append(" MMSI : ").append(this.mmsi);
//					sbTime.append(" ->");
//					sbTime.append(asmMessage);
//					sb.append(sbTime);
				}
				
				//VSI
				GroupControl groupControlVSI = new GroupControl(this.asmMessageList.size()+1, this.asmMessageList.size()+1,this.format450_ASM);
				SourceIdentification sourceIdentificationVSI = new SourceIdentification(this.jTextFieldSFI.getText());
				DestinationIdentification destinationIdentificationVSI = new DestinationIdentification("RS0001");
				TagBlock tagBlockVSI = new TagBlock(groupControlVSI, sourceIdentificationVSI, destinationIdentificationVSI);
				Formatter_61162 Formatter_61162_VSI = new Formatter_61162(tagBlockVSI);
				String Formatter_61162_VSI_message = Formatter_61162_VSI.getMessage();
				
				sb.append(SystemConstMessage.CRLF).append(TimeString.getLogDisplay(this.asmEntity.getStartTime()));
				sb.append(" MMSI : ").append(this.mmsi);
				sb.append(" ->");
				sb.append(Formatter_61162_VSI_message);
				sb.append(vsiMessage);
				
				this.format450_ASM = this.format450_ASM + 1;
				
				//CompletableFuture.runAsync(() -> this.printConsoleLog(sb.toString()));
				
//				StringBuilder sbForSend = new StringBuilder();
//				for(String asmMessage : asmMessageList) {
//					StringBuilder sbSubForSend = new StringBuilder();
//					sbSubForSend.append(Formatter_61162_message+asmMessage).append(SystemConstMessage.CRLF);
//					sbForSend.append(sbSubForSend);
//				}
				sbForSend.append(Formatter_61162_VSI_message+vsiMessage).append(SystemConstMessage.CRLF);
				CompletableFuture.runAsync(() -> this.sendTableModel.sendASMMessage(sbForSend.toString()));
				CompletableFuture.runAsync(() -> this.udpServerTableModel.sendASMMessage(sbForSend.toString()));
		}
		
		
		
		
	}
	
	
	private void printConsoleLog(String s) {
		//
		SwingUtilities.invokeLater(() -> {
			this.aisTabjTextAreaName.append(s);
		});
	}
	
	private String makeSendVsiMessage(int slotNumber, int sequence, LocalDateTime time) {
		//
		VSIMessageUtil vsiMessage = new VSIMessageUtil();
		String db = String.valueOf(RandomGenerator.generateRandomDouble(-47, -107, 1)); //Signal to noise ratio (dB) -47 ~ -107
		String dbm = String.valueOf(RandomGenerator.generateRandomDouble(5, 12, 1)); //Signal strength (dBm) of received VDL message 50 ~ 80
		
		return vsiMessage.getMessage(this.globalEntityManager.getUuid(), TimeString.getVSIFormat(time), String.valueOf(slotNumber)
				, db, dbm, String.valueOf(sequence));
	}

	public boolean isAsm() {
		return this.asm;
	}

	public void setAsm(boolean asm) {
		this.asm = asm;
		if (asm) {
			//
			this.asmEntity.setStartTime(LocalDateTime.now().plusSeconds(1), this);
		}
	}

	public boolean isVde() {
		return vde;
	}

	public void setVde(boolean vde) {
		this.vde = vde;
		if (this.vde) {
			//
			VdeEntity vdeEntity = new VdeEntity(eventPublisher);
			setVdeEntity(vdeEntity);
			vdeEntity.setStartTime(LocalDateTime.now().plusSeconds(2), this);
		}
	}

	public ApplicationEventPublisher getEventPublisher() {
		return this.eventPublisher;
	}

	public JobDetail getJob() {
		return this.startTimeJob;
	}

	public void setJob(JobDetail job) {
		this.startTimeJob = job;
	}

	public JTable getCurrentFrameJTableNameUpper() {
		return this.currentFrameJTableNameUpper;
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

	
	
	public JTextField getjTextFieldSFI() {
		return jTextFieldSFI;
	}



	public void setjTextFieldSFI(JTextField jTextFieldSFI) {
		this.jTextFieldSFI = jTextFieldSFI;
	}



	public int[] calculateSelectionInterval(int NS, double NI) {
		// 선택 간격(SI) 계산
		int lowerBound = NS - (int) (0.1 * NI); // 하한선 계산
		int upperBound = NS + (int) (0.1 * NI); // 상한선 계산

		// int[] { 최소값, 최대값 } 반환
		return new int[] { Math.max(lowerBound, 0), Math.min(upperBound, 2249) }; // 계산된 값들을 배열로 반환
	}

	public int calculateNominalIncrement(double reportRate) {
		// 명목적 증분(NI) 계산
		int nominalIncrement = (int) (2250 / reportRate);

		// 최소값과 최대값 설정
		int minimumValue = 75;
		int maximumValue = 1225;

		// 최소값과 최대값 범위 안에 있는지 확인
		if (nominalIncrement < minimumValue) {
			nominalIncrement = minimumValue;
		} else if (nominalIncrement > maximumValue) {
			nominalIncrement = maximumValue;
		}

		return nominalIncrement;
	}

	public double calculateReportRate(double reportInterval) {
		// 보고율(Rr) 계산
		double RR = 60.0 / reportInterval;

		// RR 값이 최소 2 이상, 최대 30 이하가 되도록 보정
		if (RR < 2) {
			RR = 2;
		} else if (RR > 30) {
			RR = 30;
		}
		return RR;
	}

	public int[] nArray(double RR) {
		//
		int size = (int) (RR * 0.5);
		int[] nArray = new int[size];
		for (int i = 0; i < size; i++) {
			nArray[i] = i;
		}
		return nArray;
	}

	private double[] interpolate(double[] x, double[] y) {
        int n = y.length;
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            int j = Math.min((int)Math.floor(x[i]), n - 2);
            double t = x[i] - j;
            result[i] = (1 - t) * y[j] + t * y[j + 1];
        }
        return result;
    }
	
	private double calculateCOG(double lat1, double lon1, double lat2, double lon2) {
        double dLon = Math.toRadians(lon2 - lon1);
        double y = Math.sin(dLon) * Math.cos(Math.toRadians(lat2));
        double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) -
                   Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(dLon);
        double cog = Math.toDegrees(Math.atan2(y, x));
        if (cog < 0) {
            cog += 360;
        }
        return cog;
    }
	
	@PreDestroy
	public void preDestroy() {
		//
		System.out.println("죽는다...................");
		if (startTimeJob != null && scheduler != null) {
			try {
				// Quartz 스케줄러에서 Job을 삭제
				scheduler.deleteJob(startTimeJob.getKey());
				System.out.println("Job 종료: " + startTimeJob.getKey());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return "MmsiEntity [mmsi=" + mmsi + ", nIndex=" + nIndex + ", targetChannel=" + targetChannel + ", shootCount="
				+ shootCount + ", speed=" + speed + ", RR=" + RR + ", nArray=" + Arrays.toString(nArray) + ", NI=" + NI
				+ ", NSSA=" + NSS + ", NS=" + NS + ", SI=" + Arrays.toString(SI) + ", startTime=" + startTime + "]";
	}

}
