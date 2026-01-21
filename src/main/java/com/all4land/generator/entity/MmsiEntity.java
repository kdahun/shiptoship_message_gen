package com.all4land.generator.entity;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import java.util.concurrent.CompletableFuture;

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
import com.all4land.generator.entity.event.MmsiEntityChangeStartTimeEvent;
import com.all4land.generator.entity.event.MmsiEntitySlotTimeChangeEvent;
import com.all4land.generator.entity.event.SlotTimeOutChangeEvent;
import com.all4land.generator.ui.tab.ais.model.TcpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpServerTableModel;
import com.all4land.generator.system.netty.send.config.SimpleTcpServerHandler;
import com.all4land.generator.system.netty.send.config.NettyServerTCPConfiguration;
import com.all4land.generator.system.util.BeanUtils;
import java.util.concurrent.ConcurrentMap;
import io.netty.channel.Channel;
import com.all4land.generator.util.TimeString;
import com.all4land.generator.util.RandomGenerator;

import dk.dma.ais.sentence.Vdm;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;

import com.all4land.generator.system.mqtt.MqttClientConfiguration;

@Component
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

	// destMMSI 리스트 (thread-safe)
	private List<Long> destMMSIList = new CopyOnWriteArrayList<>();

	private int slotTimeOut_default;		// 테스트용 slotTimeOut testInit에서 초기화
	private int slotTimeOut = RandomGenerator.generateRandomIntFromTo(0, 7);	// AIS 메시지 타임아웃 시작 시간

	private AsmEntity asmEntity;	// ASM 메시지 관리 Entity
	private VdeEntity vdeEntity;	// VDE 메시지 관리 Entity

	// AIS 점유 슬롯 리스트
	private List<TargetSlotEntity> targetSlotEntity = new ArrayList<>();

	// slot time out 시작 시간
	private LocalDateTime slotTimeOutTime;

	// AIS Job
	private JobDetail startTimeJob;

	// slot time out Job
	private JobDetail slotTimeOutJob;
	private final Scheduler scheduler;

	// UI 제거로 인해 주석 처리
	// private JTextArea aisTabjTextAreaName;

	private GlobalEntityManager globalEntityManager;

	// UI 제거로 인해 주석 처리
	// private JTable currentFrameJTableNameUpper;
	// private JTable currentFrame1JTableNameUpper;
	// private JTable currentFrame2JTableNameUpper;
	// private JTable currentFrame3JTableNameUpper;
	// private JTable currentFrame4JTableNameUpper;
	// private JTable currentFrame5JTableNameUpper;
	// private JTable currentFrame6JTableNameUpper;
	// private JTable currentFrame7JTableNameUpper;
	//
	// private JTable currentFrameJTableNameLower;
	// private JTable currentFrame1JTableNameLower;
	// private JTable currentFrame2JTableNameLower;
	// private JTable currentFrame3JTableNameLower;
	// private JTable currentFrame4JTableNameLower;
	// private JTable currentFrame5JTableNameLower;
	// private JTable currentFrame6JTableNameLower;
	// private JTable currentFrame7JTableNameLower;

	private final ApplicationEventPublisher eventPublisher;
	
	// UI 제거로 인해 주석 처리
	// private JTextField jTextFieldSFI;
	private String sfiValue; // UI 제거 후 SFI 값을 문자열로 저장

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

	public List<TargetSlotEntity> getTargetSlotEntity() {
		return this.targetSlotEntity;
	}

	public void setTargetSlotEntity(List<TargetSlotEntity> targetSlotEntity) {
		this.targetSlotEntity = targetSlotEntity;
	}

	public void addTargetSlotEntity(TargetSlotEntity targetSlotEntity) {
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
			SlotTimeOutChangeEvent event = new SlotTimeOutChangeEvent(this, 'A', this);

			eventPublisher.publishEvent(event);
		}

	}

	// UI 제거로 인해 주석 처리
	// public JTextArea getAisTabjTextAreaName() {
	//	return this.aisTabjTextAreaName;
	// }
	//
	// public void setAisTabjTextAreaName(JTextArea aisTabjTextAreaName) {
	//	this.aisTabjTextAreaName = aisTabjTextAreaName;
	// }

	/**
	 * AIS 메시지 생성 여부 확인
	 * destMMSI 리스트가 비어있지 않을 때만 메시지 생성 (destMMSI가 있는 경우만 메시지 생성)
	 * destMMSI 리스트가 비어있으면 자동으로 setChk(false) 호출하여 스케줄러 job 제거
	 */
	public boolean isChk() {
		// destMMSI 리스트가 비어있지 않을 때만 메시지 생성
		if (!this.destMMSIList.isEmpty()) {
			// destMMSI가 있고 chk가 true일 때만 true 반환
			return this.chk;
		}
		// destMMSI 리스트가 비어있으면 메시지 생성하지 않음
		// chk가 true인 상태에서 destMMSI가 비어있으면 setChk(false) 호출하여 스케줄러 job 제거
		if (this.chk) {
			System.out.println("[DEBUG] isChk() 체크: destMMSI 리스트가 비어있어 setChk(false) 호출 - MMSI: " + this.mmsi);
			this.setChk(false);
		}
		return false;
	}

	/**
	 * [MMSI_AIS_FLOW]-2-2
	 * QuartzCoreService Scheduler에 Job등록을 위한 함수, 시작 시간 설정 후 RR, NI 초기화
	 * To [MMSI_AIS_FLOW]-3 MmsiEntity.setStartTime
	 */
	public void setChk(boolean chk) {
		System.out.println("[DEBUG] MmsiEntity.setChk() 호출 - MMSI: " + this.mmsi + ", chk: " + chk);
		this.chk = chk;
		if (!this.chk) {
			//
			System.out.println("[DEBUG] AIS 비활성화 - MMSI: " + this.mmsi);
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
			System.out.println("[DEBUG] AIS 활성화 시작 - MMSI: " + this.mmsi);
//			this.setCalculateBasic();
			LocalDateTime startTime = RandomGenerator.generateRandomLocalDateTimeFormToAddMillisec(3500, 10502);
			this.setStartTime(startTime);
			System.out.println("[DEBUG] 시작 시간 설정 완료 - MMSI: " + this.mmsi + ", startTime: " + startTime);
//			int randomDelay = RandomGenerator.generateRandomIntFromTo(3, 10);
//			this.setStartTime(LocalDateTime.now().plusSeconds(randomDelay));
			this.resetRRAndNI();
			System.out.println("[DEBUG] RR, NI 초기화 완료 - MMSI: " + this.mmsi + ", RR: " + this.RR + ", NI: " + this.NI);
		}
	}

	/**
	 * destMMSI를 리스트에 추가
	 * @param destMMSI 추가할 목적지 MMSI
	 */
	public void addDestMMSI(long destMMSI) {
		if (!this.destMMSIList.contains(destMMSI)) {
			this.destMMSIList.add(destMMSI);
			System.out.println("[DEBUG] destMMSI 추가 - MMSI: " + this.mmsi + ", destMMSI: " + destMMSI + 
					", 리스트 크기: " + this.destMMSIList.size());
		} else {
			System.out.println("[DEBUG] destMMSI 중복 - MMSI: " + this.mmsi + ", destMMSI: " + destMMSI);
		}
	}

	/**
	 * destMMSI를 리스트에서 제거
	 * 리스트가 비어지면 자동으로 setChk(false) 호출하여 메시지 생성 중단
	 * @param destMMSI 제거할 목적지 MMSI
	 */
	public void removeDestMMSI(long destMMSI) {
		if (this.destMMSIList.remove(destMMSI)) {
			System.out.println("[DEBUG] destMMSI 제거 - MMSI: " + this.mmsi + ", destMMSI: " + destMMSI + 
					", 리스트 크기: " + this.destMMSIList.size());
			
			// destMMSI 리스트가 비어지면 메시지 생성 중단
			if (this.destMMSIList.isEmpty() && this.chk) {
				System.out.println("[DEBUG] destMMSI 리스트가 비어서 메시지 생성 중단 - MMSI: " + this.mmsi);
				this.setChk(false);
			}
		} else {
			System.out.println("[DEBUG] destMMSI 없음 - MMSI: " + this.mmsi + ", destMMSI: " + destMMSI);
		}
	}

	/**
	 * destMMSI 리스트 반환
	 * @return destMMSI 리스트
	 */
	public List<Long> getDestMMSIList() {
		return new ArrayList<>(this.destMMSIList); // 방어적 복사
	}

	/**
	 * destMMSI 리스트가 비어있지 않은지 확인
	 * @return 리스트가 비어있지 않으면 true
	 */
	public boolean hasDestMMSI() {
		return !this.destMMSIList.isEmpty();
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
		System.out.println("[DEBUG] MmsiEntity.setStartTime() 호출 - MMSI: " + this.mmsi + 
				", startTime: " + startTime);
		this.startTime = startTime;

		MmsiEntityChangeStartTimeEvent event = new MmsiEntityChangeStartTimeEvent(this, "startTime", this.startTime,
				startTime, this);
		System.out.println("[DEBUG] MmsiEntityChangeStartTimeEvent 발행 - MMSI: " + this.mmsi);
		eventPublisher.publishEvent(event);
		System.out.println("[DEBUG] 이벤트 발행 완료 - MMSI: " + this.mmsi);
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
			SourceIdentification sourceIdentification = new SourceIdentification(this.sfiValue != null ? this.sfiValue : "");
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
			SourceIdentification sourceIdentificationVSI = new SourceIdentification(this.sfiValue != null ? this.sfiValue : "");
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

			// TCP로 전송하는 메시지
			CompletableFuture.runAsync(() -> {
				System.out.println("[DEBUG] AIS message sending - MMSI: " + this.mmsi + ", Slot: " + slotNumber);
				System.out.println("[DEBUG] AIS message sending: " + Formatter_61162_message+aisMessage);
				System.out.println("[DEBUG] VSI message sending: " + Formatter_61162_VSI_message+vsiMessage);
				
				// TcpServerTableModel을 통한 전송 (UI 모드)
				if (this.sendTableModel != null) {
					System.out.println("[DEBUG] TcpServerTableModel을 통한 전송 시도");
					this.sendTableModel.sendAISMessage(
							Formatter_61162_message+aisMessage, Formatter_61162_VSI_message+vsiMessage);
				}
				// SimpleTcpServerHandler를 통한 직접 전송 (headless 모드)
				System.out.println("[DEBUG] SimpleTcpServerHandler를 통한 전송 시도");
				this.sendAISMessageToSimpleTcpClients(
						Formatter_61162_message+aisMessage, Formatter_61162_VSI_message+vsiMessage);
			});
			
			CompletableFuture.runAsync(() -> {
				if (this.udpServerTableModel != null) {
					this.udpServerTableModel.sendAISMessage(
							Formatter_61162_message+aisMessage, Formatter_61162_VSI_message+vsiMessage);
				}
			});

			// MQTT로 AIS 메시지 전송
			CompletableFuture.runAsync(() -> {
				try {
					MqttClientConfiguration mqttClient = (MqttClientConfiguration) BeanUtils.getBean("mqttClient");
					if (mqttClient != null && mqttClient.isConnected()) {
						String aisMessageToSend = aisMessage + SystemConstMessage.CRLF + vsiMessage;
						
						// JSON 형태로 변환 : [{"destMMSI":["440123456", "440654321"], "NMEA":"!AIVDM..."}]
						Map<String, Object> nmeaObject = new HashMap<>();
						nmeaObject.put("NMEA", aisMessageToSend);
						
						// destMMSI 리스트 추가
						List<Long> destMMSIList = this.getDestMMSIList();
						if (destMMSIList != null && !destMMSIList.isEmpty()) {
							List<String> destMMSIStrList = destMMSIList.stream()
									.map(String::valueOf)
									.collect(Collectors.toList());
							nmeaObject.put("destMMSI", destMMSIStrList);
							System.out.println("[DEBUG] destMMSI 포함 - MMSI: " + this.mmsi + 
									", destMMSI: " + destMMSIStrList);
						}
						
						List<Map<String, Object>> jsonArray = new ArrayList<>();
						jsonArray.add(nmeaObject);

						Gson gson = new Gson();
						String jsonMessage = gson.toJson(jsonArray);

						// 동적 토픽 생성: mg/ms/ais/{mmsi}/{yyyyMMddHHmmss.ssss}
						DateTimeFormatter topicFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSS");
						String timestamp = LocalDateTime.now().format(topicFormatter);
						String topic = String.format("mg/ms/ais/%d/%s", this.mmsi, timestamp);
						
						mqttClient.publish(topic, jsonMessage, 0, false);
						System.out.println("[DEBUG] ✅ MQTT로 AIS 메시지 전송 완료: MMSI=" + this.mmsi + ", Topic=" + topic);
					} else {
						System.out.println("[DEBUG] ⚠️ MQTT 클라이언트가 연결되지 않았거나 사용할 수 없습니다.");
					}
				} catch (Exception e) {
					System.out.println("[DEBUG] ❌ MQTT AIS 메시지 전송 실패: " + e.getMessage());
					e.printStackTrace();
				}
			});
		}
	}
		
	public void setAsmMessageList(List<String> asmMessageList, String slotNumber) {
		//
		String vsiMessage = this.makeSendVsiMessage(Integer.parseInt(slotNumber), this.getAsmMessageSequence(), this.asmEntity.getStartTime());
		
		this.asmMessageList = asmMessageList;
		StringBuilder sb = new StringBuilder();
		StringBuilder sbForSend = new StringBuilder();
		StringBuilder sbForSendMqtt = new StringBuilder();

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
					SourceIdentification sourceIdentification = new SourceIdentification(this.sfiValue != null ? this.sfiValue : "");
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
					sbForSendMqtt.append(this.asmMessageList.get(i)).append(SystemConstMessage.CRLF);
//					StringBuilder sbTime = new StringBuilder(SystemConstMessage.CRLF)
//							.append(TimeString.getLogDisplay(this.asmEntity.getStartTime()));
//					sbTime.append(" MMSI : ").append(this.mmsi);
//					sbTime.append(" ->");
//					sbTime.append(asmMessage);
//					sb.append(sbTime);
				}
				
				//VSI
				GroupControl groupControlVSI = new GroupControl(this.asmMessageList.size()+1, this.asmMessageList.size()+1,this.format450_ASM);
				SourceIdentification sourceIdentificationVSI = new SourceIdentification(this.sfiValue != null ? this.sfiValue : "");
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
				
				CompletableFuture.runAsync(() -> this.printConsoleLog(sb.toString()));
				
//				StringBuilder sbForSend = new StringBuilder();
//				for(String asmMessage : asmMessageList) {
//					StringBuilder sbSubForSend = new StringBuilder();
//					sbSubForSend.append(Formatter_61162_message+asmMessage).append(SystemConstMessage.CRLF);
//					sbForSend.append(sbSubForSend);
//				}
				sbForSend.append(Formatter_61162_VSI_message+vsiMessage).append(SystemConstMessage.CRLF);
				sbForSendMqtt.append(vsiMessage).append(SystemConstMessage.CRLF);

				CompletableFuture.runAsync(() -> {
					System.out.println("[DEBUG] ASM message sending - MMSI: " + this.mmsi + ", Slot: " + slotNumber);
					System.out.println("[DEBUG] ASM message sending: " + sbForSend.toString());
					
					// TcpServerTableModel을 통한 전송 (UI 모드)
					if (this.sendTableModel != null) {
						System.out.println("[DEBUG] TcpServerTableModel을 통한 ASM 전송 시도");
						this.sendTableModel.sendASMMessage(sbForSend.toString());
					}
					// SimpleTcpServerHandler를 통한 직접 전송 (headless 모드)
					System.out.println("[DEBUG] SimpleTcpServerHandler를 통한 ASM 전송 시도");
					this.sendASMMessageToSimpleTcpClients(sbForSend.toString());
				});
				
				CompletableFuture.runAsync(() -> {
					if (this.udpServerTableModel != null) {
						this.udpServerTableModel.sendASMMessage(sbForSend.toString());
					}
				});


				// MQTT로 ASM 메시지 전송
				CompletableFuture.runAsync(() -> {
					try {
						MqttClientConfiguration mqttClient = (MqttClientConfiguration) BeanUtils.getBean("mqttClient");
						if (mqttClient != null && mqttClient.isConnected()) {
							
							// JSON 형태로 변환 : [{"NMEA":"!AIVDM..."}]
							String mqttMessage = sbForSendMqtt.toString();
							Map<String, String> nmeaObject = new HashMap<>();
							nmeaObject.put("NMEA", mqttMessage);
							List<Map<String, String>> jsonArray = new ArrayList<>();
							jsonArray.add(nmeaObject);

							Gson gson = new Gson();
							String jsonMessage = gson.toJson(jsonArray);
							
							
							// 동적 토픽 생성: mg/ms/asm/{mmsi}/{yyyyMMddHHmmss.ssss}/{sbForSendMqtt 내용}
							DateTimeFormatter topicFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSS");
							String timestamp = LocalDateTime.now().format(topicFormatter);
							String topic = String.format("mg/ms/asm/%d/%s", this.mmsi, timestamp);
							
							mqttClient.publish(topic, jsonMessage, 0, false);
							System.out.println("[DEBUG] ✅ MQTT로 ASM 메시지 전송 완료: MMSI=" + this.mmsi + ", Slot=" + slotNumber + ", Topic=" + topic);
						} else {
							System.out.println("[DEBUG] ⚠️ MQTT 클라이언트가 연결되지 않았거나 사용할 수 없습니다.");
						}
					} catch (Exception e) {
						System.out.println("[DEBUG] ❌ MQTT ASM 메시지 전송 실패: " + e.getMessage());
						e.printStackTrace();
					}
				});
		}
	}
	
	
	private void printConsoleLog(String s) {
		//
		// UI 제거로 인해 주석 처리 - 로그는 System.out으로 출력
		System.out.println(s);
		// SwingUtilities.invokeLater(() -> {
		//	this.aisTabjTextAreaName.append(s);
		// });
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

	// UI 제거로 인해 주석 처리
	// public JTable getCurrentFrameJTableNameUpper() {
	//	return this.currentFrameJTableNameUpper;
	// }
	//
	// public void setCurrentFrameJTableNameUpper(JTable currentFrameJTableNameUpper) {
	//	this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
	// }
	//
	// ... (모든 JTable getter/setter 주석 처리)
	
	public String getSfiValue() {
		return sfiValue;
	}

	public void setSfiValue(String sfiValue) {
		this.sfiValue = sfiValue;
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
        
        // 단일 점만 있는 경우 (n == 1) 모든 결과를 같은 값으로 설정
        if (n == 1) {
            for (int i = 0; i < x.length; i++) {
                result[i] = y[0];
            }
            return result;
        }
        
        // 최소 2개 이상의 점이 있는 경우에만 보간 수행
        for (int i = 0; i < x.length; i++) {
            // x[i] 값을 [0, n-1] 범위로 정규화
            double normalizedX = Math.max(0.0, Math.min(x[i], n - 1.0));
            int j = Math.min((int)Math.floor(normalizedX), n - 2);
            // j가 음수가 되지 않도록 보정
            j = Math.max(0, j);
            double t = normalizedX - j;
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
	
	/**
	 * SimpleTcpServerHandler를 통해 연결된 모든 클라이언트에게 AIS 메시지 전송
	 */
	private void sendAISMessageToSimpleTcpClients(String aisMessage, String vsiMessage) {
		try {
			// TCP 서버 포트 (ApplicationInitializer에서 설정한 포트)
			int tcpPort = 10110;
			String beanName = tcpPort + "_TcpServer";
			
			System.out.println("[DEBUG] sendAISMessageToSimpleTcpClients 시작 - 포트: " + tcpPort);
			
			// TCP 서버 설정 가져오기
			NettyServerTCPConfiguration tcpServer = (NettyServerTCPConfiguration) BeanUtils.getBean(beanName);
			if (tcpServer == null) {
				System.out.println("[DEBUG] TCP 서버를 찾을 수 없음: " + beanName);
				return;
			}
			System.out.println("[DEBUG] TCP 서버 찾음: " + beanName);
			
			// 연결된 클라이언트 확인
			ConcurrentMap<String, Channel> clients = SimpleTcpServerHandler.getAllClients();
			System.out.println("[DEBUG] 연결된 클라이언트 수: " + clients.size());
			if (clients.isEmpty()) {
				System.out.println("[DEBUG] 연결된 클라이언트가 없습니다!");
				return;
			}
			
			// 메시지 조합
			StringBuilder sb = new StringBuilder();
			// AIS 메시지에 CRLF가 없으면 추가
			String aisMsg = aisMessage.endsWith(SystemConstMessage.CRLF) ? aisMessage : aisMessage + SystemConstMessage.CRLF;
			sb.append(aisMsg);
			// VSI 메시지에 CRLF가 없으면 추가
			String vsiMsg = vsiMessage.endsWith(SystemConstMessage.CRLF) ? vsiMessage : vsiMessage + SystemConstMessage.CRLF;
			sb.append(vsiMsg);
			String message = sb.toString();
			System.out.println("[DEBUG] 전송할 메시지 길이: " + message.length() + " bytes");
			
			// 연결된 모든 클라이언트에게 메시지 전송
			clients.forEach((clientKey, channel) -> {
				System.out.println("[DEBUG] 클라이언트 처리 시작: " + clientKey);
				if (channel != null && channel.isActive()) {
					System.out.println("[DEBUG] 채널 활성 상태 확인: " + clientKey + " - isActive: " + channel.isActive());
					try {
						String[] parts = clientKey.split(":");
						if (parts.length == 2) {
							String clientIP = parts[0];
							int clientPort = Integer.parseInt(parts[1]);
							System.out.println("[DEBUG] 메시지 전송 시도: " + clientIP + ":" + clientPort);
							tcpServer.sendToClient(channel, clientIP, clientPort, message);
							System.out.println("[DEBUG] ✅ AIS 메시지 전송 완료: " + clientIP + ":" + clientPort);
						} else {
							System.out.println("[DEBUG] 잘못된 클라이언트 키 형식: " + clientKey);
						}
					} catch (Exception e) {
						System.out.println("[DEBUG] ❌ AIS 메시지 전송 실패: " + clientKey + " - " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					System.out.println("[DEBUG] 채널이 null이거나 비활성 상태: " + clientKey + " - channel: " + (channel != null) + ", isActive: " + (channel != null ? channel.isActive() : false));
				}
			});
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ AIS 메시지 전송 중 오류: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * SimpleTcpServerHandler를 통해 연결된 모든 클라이언트에게 ASM 메시지 전송
	 */
	private void sendASMMessageToSimpleTcpClients(String asmMessageAndVsiMessage) {
		try {
			// TCP 서버 포트 (ApplicationInitializer에서 설정한 포트)
			int tcpPort = 10110;
			String beanName = tcpPort + "_TcpServer";
			
			System.out.println("[DEBUG] sendASMMessageToSimpleTcpClients 시작 - 포트: " + tcpPort);
			
			// TCP 서버 설정 가져오기
			NettyServerTCPConfiguration tcpServer = (NettyServerTCPConfiguration) BeanUtils.getBean(beanName);
			if (tcpServer == null) {
				System.out.println("[DEBUG] TCP 서버를 찾을 수 없음: " + beanName);
				return;
			}
			System.out.println("[DEBUG] TCP 서버 찾음: " + beanName);
			
			// 연결된 클라이언트 확인
			ConcurrentMap<String, Channel> clients = SimpleTcpServerHandler.getAllClients();
			System.out.println("[DEBUG] 연결된 클라이언트 수: " + clients.size());
			if (clients.isEmpty()) {
				System.out.println("[DEBUG] 연결된 클라이언트가 없습니다!");
				return;
			}
			
			System.out.println("[DEBUG] 전송할 ASM 메시지 길이: " + asmMessageAndVsiMessage.length() + " bytes");
			
			// 연결된 모든 클라이언트에게 메시지 전송
			clients.forEach((clientKey, channel) -> {
				System.out.println("[DEBUG] ASM 클라이언트 처리 시작: " + clientKey);
				if (channel != null && channel.isActive()) {
					System.out.println("[DEBUG] ASM 채널 활성 상태 확인: " + clientKey + " - isActive: " + channel.isActive());
					try {
						String[] parts = clientKey.split(":");
						if (parts.length == 2) {
							String clientIP = parts[0];
							int clientPort = Integer.parseInt(parts[1]);
							System.out.println("[DEBUG] ASM 메시지 전송 시도: " + clientIP + ":" + clientPort);
							tcpServer.sendToClient(channel, clientIP, clientPort, asmMessageAndVsiMessage);
							System.out.println("[DEBUG] ✅ ASM 메시지 전송 완료: " + clientIP + ":" + clientPort);
						} else {
							System.out.println("[DEBUG] 잘못된 클라이언트 키 형식: " + clientKey);
						}
					} catch (Exception e) {
						System.out.println("[DEBUG] ❌ ASM 메시지 전송 실패: " + clientKey + " - " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					System.out.println("[DEBUG] ⚠️ ASM 채널이 비활성 상태: " + clientKey);
				}
			});
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ ASM 메시지 전송 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
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
