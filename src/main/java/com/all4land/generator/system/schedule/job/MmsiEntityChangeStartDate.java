package com.all4land.generator.system.schedule.job;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.all4land.generator.ais.AisMessage1Util;
import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.entity.TargetSlotEntity;
import com.all4land.generator.system.constant.SystemConstMessage;

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
	 * QuarztCoreService ScheduleJob의 Trigger에 등록된 시작시간이 되면 해당 execute 호출로 mainProcess에서 ais메시지 생성 등 처리
	 * 프로세스가 끝난 뒤 addFuture에서 다음 MmsiEntity 처리를 위한 setStartTime 지정
	 * To [MMSI_AIS_FLOW]-6-1 MmsiEntityChangeStartDate.mainProcess
	 * To [MMSI_AIS_FLOW]-6-2 MmsiEntityChangeStartDate.addFuture
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("[DEBUG] ========== MmsiEntityChangeStartDate.execute() 시작 ==========");
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		this.mmsiEntity = (MmsiEntity) jobDataMap.get("mmsiEntity");
		System.out.println("[DEBUG] Quartz Job 실행 - MMSI: " + 
				(this.mmsiEntity != null ? this.mmsiEntity.getMmsi() : "null") + 
				", StartTime: " + (this.mmsiEntity != null ? this.mmsiEntity.getStartTime() : "null"));
		try {
			this.mainProcess();
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("[DEBUG] ❌ mainProcess() 실행 중 오류 발생");
			e.printStackTrace();
		}
		this.addFuture();
		System.out.println("[DEBUG] ========== MmsiEntityChangeStartDate.execute() 종료 ==========");
	}

	/**
	 * [MMSI_AIS_FLOW]-6-1
	 * AIS 메시지 생성 및 처리.
	 * 1. mmsiEntity가 활성화되어 있는지 확인
	 * 2. 이전에 전송된 슬롯 확인
	 * 3. 슬롯이 없으면 SI 계산
	 * 4. 마킹, 전송
	 */
	private void mainProcess() {
		//
		System.out.println("[DEBUG] mainProcess() 시작 - MMSI: " + this.mmsiEntity.getMmsi());
		/**
		 * mmsiEntity가 활성화되어 있는지 확인
		 * 화면상에서의 CheckBox
		 */
		if (this.mmsiEntity.isChk()) {
			System.out.println("[DEBUG] ✅ MmsiEntity 활성화 확인 - MMSI: " + this.mmsiEntity.getMmsi());
			//
			/** 
			 * 이전에 전송된 슬롯 확인
			 */ 
			TargetSlotEntity rtnValue = this.checkHistorySlot();
			System.out.println("[DEBUG] checkHistorySlot() 결과 - MMSI: " + this.mmsiEntity.getMmsi() + 
					", rtnValue: " + (rtnValue != null ? "있음 (slotNumber: " + rtnValue.getSlotNumber() + ")" : "없음"));
			
			if (rtnValue != null) {
				// 
				System.out.println("[DEBUG] 이전 슬롯 사용 - MMSI: " + this.mmsiEntity.getMmsi() + 
						", SlotNumber: " + rtnValue.getSlotNumber());
				Vdm vdm = AisMessage1Util.create(this.mmsiEntity, rtnValue.getSlotNumber());
				System.out.println("[DEBUG] ✅ AIS 메시지 생성 완료 (이전 슬롯) - MMSI: " + this.mmsiEntity.getMmsi() + 
						", SlotNumber: " + rtnValue.getSlotNumber());
				this.mmsiEntity.setMessage1(vdm, rtnValue.getSlotNumber());
				this.mmsiEntity.setAisMessageSequence(mmsiEntity.getAisMessageSequence()+1);
				
			} else {
				//
				System.out.println("[DEBUG] 새 슬롯 찾기 시작 - MMSI: " + this.mmsiEntity.getMmsi());
				// SI 계산
				this.mmsiEntity.setSelectionInterval();
				System.out.println("[DEBUG] SI 계산 완료 - MMSI: " + this.mmsiEntity.getMmsi() + 
						", SI: " + this.mmsiEntity.getSI());
				// 마킹 및 전송
				// int successSlotNumber = 
				int result = this.globalEntityManager.findSlotAndMarking(mmsiEntity);
				System.out.println("[DEBUG] findSlotAndMarking() 결과 - MMSI: " + this.mmsiEntity.getMmsi() + 
						", result: " + result);
				if (result <= -1) {
					System.out.println("[DEBUG] ❌ 슬롯 찾기 실패 - MMSI: " + this.mmsiEntity.getMmsi() + 
							", result: " + result);
				} else {
					System.out.println("[DEBUG] ✅ 슬롯 찾기 성공 - MMSI: " + this.mmsiEntity.getMmsi() + 
							", slotNumber: " + result);
				}
			}
		} else {
			System.out.println("[DEBUG] ❌ MmsiEntity 비활성화 상태 - MMSI: " + this.mmsiEntity.getMmsi() + 
					", chk: " + this.mmsiEntity.isChk());
		}
	}

	/**
	 * [MMSI_AIS_FLOW]-6-2
	 * 1. startTime 설정(startTime + speed) -> 다음 시작시간 설정을 위한 QuartzCoreService.addScheduleJob이 호출됨
	 * 2. 전송 카운트 업
	 * 3. 슬롯 최대값 2249를 넘어가면 초기화
	 * 	- NS, NI의 의미 분석 필요
	 *	- NSS = 시작시간(startTime)에 해당하는 SlotNumber로 초기화
	 */
	private void addFuture() {
		//
		/**
		 * startTime 설정(startTime + speed)
		 */
		LocalDateTime nextStartTime = this.mmsiEntity.getStartTime().plusSeconds(this.mmsiEntity.getSpeed());
		this.mmsiEntity.setStartTime(nextStartTime);

		/**
		 * 전송 카운트 업
		 */
		this.mmsiEntity.setShootCount(this.mmsiEntity.getShootCount() + 1);

		/**
		 * (NS + NI)가 슬롯 최대값 2249를 넘어가면 초기화
		 */
		if ((this.mmsiEntity.getNS() + this.mmsiEntity.getNI()) > 2249) {
			//
			this.mmsiEntity.setnIndex(0);
			this.mmsiEntity.setNSS(this.mmsiEntity.getStartSlotNumber());
			this.mmsiEntity.clearShootCount(-1);
		}
	}

	/**
	 * 이전에 전송된 슬롯을 확인해, 동일한 슬롯을 다시 사용할 수 있는지 확인 및 해당 SlotEntity 반환
	 * @return TargetSlotEntity 이 전에 전송된 슬롯이 있으면 해당 SlotEntity 반환, 없으면 null 반환
	 */
	private TargetSlotEntity checkHistorySlot() {
		//
		LocalDateTime startTime = this.mmsiEntity.getStartTime();
		String startTime_ssSSSS = startTime.format(SystemConstMessage.formatterForStartIndex);
		double startTime_currentSecond = Double.parseDouble(startTime_ssSSSS) - 0.300;

		LocalDateTime endTime = this.mmsiEntity.getStartTime();
		String endTime_ssSSSS = endTime.format(SystemConstMessage.formatterForStartIndex);
		double endTime_currentSecond = Double.parseDouble(endTime_ssSSSS) + 0.300;

		// -------------------------- targetslotentity 분석
		/**
		 * addTargetSlotEntity : GlobalEntityManager.findSlotAndMarking -> addAISTargetSlotEntity에서 호출.
		 * clearTargetSlotEntity : MmsiEntitySlotTimeChangeQuartz Job에서 호출(슬롯 초기화 Job)
		 */
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




