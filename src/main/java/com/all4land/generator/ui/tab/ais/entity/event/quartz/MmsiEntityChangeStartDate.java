package com.all4land.generator.ui.tab.ais.entity.event.quartz;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.all4land.generator.ais.AisMessage1Util;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.ui.tab.ais.entity.GlobalEntityManager;
import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.ui.tab.ais.entity.TargetSlotEntity;

import dk.dma.ais.sentence.Vdm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MmsiEntityChangeStartDate implements Job {
	//
	private final GlobalEntityManager globalEntityManager;
	private MmsiEntity mmsiEntity;

	public MmsiEntityChangeStartDate(GlobalEntityManager globalEntityManager) {
		// TODO Auto-generated constructor stub
		this.globalEntityManager = globalEntityManager;
	}

	/**
	 * [MMSI_AIS_FLOW]-6
	 * Trigger에 등록된 시작시간이 되면 해당 execute 호출로 mainProcess에서 ais메시지 생성 등 처리
	 * 프로세스가 끝난 뒤 addFuture에서 다음 MmsiEntity 처리를 위한 setStartTime 지정
	 * To [MMSI_AIS_FLOW]-6-1 MmsiEntityChangeStartDate.mainProcess
	 * To [MMSI_AIS_FLOW]-6-2 MmsiEntityChangeStartDate.addFuture
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		log.info("{} , {}", LocalDateTime.now(), "시작");
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		this.mmsiEntity = (MmsiEntity) jobDataMap.get("mmsiEntity");
		 log.info("{} , {}", LocalDateTime.now(), this.mmsiEntity.getStartTime());
		try {
//			log.info("1");
			this.mainProcess();
//			log.info("10");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// log.info("{} , {}", LocalDateTime.now(), this.mmsiEntity.getStartTime());
//		log.info("11");
		this.addFuture();
		// log.info("{} , {}", LocalDateTime.now(), this.mmsiEntity.getStartTime());
		// log.info("");
	}

	/**
	 * [MMSI_AIS_FLOW]-6-1
	 * AIS 메시지 생성 및 처리. 추가 분석 필요
	 */
	private void mainProcess() {
		//
//		log.info("2");
		// mmsiEntity.Chk ? 1. MMSI 생성되면 True로 초기화 (GlobalEntityManager.addMmsiEntity)
		// 2. MmsiTableModel.setValueAt 에서 input parameter value(Object)에 따라 변경
		if (this.mmsiEntity.isChk()) {
			//
			// 1 기존에 쏜 히스토리확인
//			log.info("3");
			TargetSlotEntity rtnValue = this.checkHistorySlot();
//			log.info("4");
			if (rtnValue != null) {
				// 있으면 바로 쏜다
//				log.info("55");
				Vdm vdm = AisMessage1Util.create(this.mmsiEntity, rtnValue.getSlotNumber());
				this.mmsiEntity.setMessage1(vdm, rtnValue.getSlotNumber());
				this.mmsiEntity.setAisMessageSequence(mmsiEntity.getAisMessageSequence()+1);
				
				
			} else {
				//
				// SI 계산
//				log.info("5");
				this.mmsiEntity.setSelectionInterval();
//				log.info("6");
				// 마킹하고 쏜다
				int successSlotNumber = this.globalEntityManager.findSlotAndMarking(mmsiEntity);
//				log.info("8");
//				if(successSlotNumber <= -1) {
//					log.info("점유 못함1. MMSI : {}, NS : {} , {}", this.mmsiEntity.getMmsi(), this.mmsiEntity.getNS(),
//							this.mmsiEntity);
//				}else {
//					log.info("점유 성공1. MMSI : {}, NS : {} , {}", this.mmsiEntity.getMmsi(), this.mmsiEntity.getNS(),
//							this.mmsiEntity);
//				}
//				log.info("NSS : {}", this.mmsiEntity.getNSSA());
//				log.info("MMSI : {}, NS : {} , {}", this.mmsiEntity.getMmsi(), this.mmsiEntity.getNS(),
//						this.mmsiEntity);
//				log.info("SI : {}", this.mmsiEntity.getSI());

			}
//			log.info("");
		}
	}

	/**
	 * [MMSI_AIS_FLOW]-6-2
	 * 분석 필요
	 */
	private void addFuture() {
		//
//		log.info("변경전 : "+this.mmsiEntity.getStartTime().toString());
		LocalDateTime newLocalDateTime = this.mmsiEntity.getStartTime().plusSeconds(this.mmsiEntity.getSpeed());
//		log.info("bbbbbbbbbb");
		this.mmsiEntity.setStartTime(newLocalDateTime);
//		log.info("변경후 : "+this.mmsiEntity.getStartTime().toString());
		// 카운트하고
//		log.info("1");
		this.mmsiEntity.setShootCount(this.mmsiEntity.getShootCount() + 1);
//		log.info("2");
		if ((this.mmsiEntity.getNS() + this.mmsiEntity.getNI()) > 2249) {
			//
//			log.info("처음부터 다시해야한다.");
			this.mmsiEntity.setnIndex(0);
//			this.mmsiEntity.setNSS(-1);
			this.mmsiEntity.setNSS(this.mmsiEntity.getStartSlotNumber());
			this.mmsiEntity.clearShootCount(-1);
		}
		
	}

	private TargetSlotEntity checkHistorySlot() {
		//
		LocalDateTime startTime = this.mmsiEntity.getStartTime();
		String startTime_ssSSSS = startTime.format(SystemConstMessage.formatterForStartIndex);
		double startTime_currentSecond = Double.parseDouble(startTime_ssSSSS) - 0.300;

		LocalDateTime endTime = this.mmsiEntity.getStartTime();
		String endTime_ssSSSS = endTime.format(SystemConstMessage.formatterForStartIndex);
		double endTime_currentSecond = Double.parseDouble(endTime_ssSSSS) + 0.300;

		for (TargetSlotEntity entity : this.mmsiEntity.getTargetSlotEntity()) {
			//
			if (entity != null) {
				//
				double target_ssSSSS = entity.getSsSSSS();
				if (target_ssSSSS >= startTime_currentSecond && target_ssSSSS <= endTime_currentSecond) {
					//
					return entity;
				}
			}
		}
		return null;
	}

}
