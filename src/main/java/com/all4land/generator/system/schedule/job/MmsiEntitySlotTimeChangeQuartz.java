package com.all4land.generator.system.schedule.job;

import java.text.ParseException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.entity.SlotStateManager;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.util.RandomGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MmsiEntitySlotTimeChangeQuartz implements Job {
	//
	private MmsiEntity mmsiEntity;
	private final QuartzCoreService quartzCoreService;
	private final SlotStateManager slotStateManager;
	
	MmsiEntitySlotTimeChangeQuartz(QuartzCoreService quartzCoreService, SlotStateManager slotStateManager){
		//
		this.quartzCoreService = quartzCoreService;
		this.slotStateManager = slotStateManager;
	}
	
	/**
	 * [SLOT_TIME_FLOW]-4
	 * 트리거 발동 시 스케쥴된 Job 실행
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		this.mmsiEntity = (MmsiEntity) jobDataMap.get("mmsiEntity");
		
		this.process();
	}

	/**
	 * SlotTimeChangeQuartz Job 실행 처리
	 */
	private void process() {
		//
		System.out.println("[DEBUG] MmsiEntitySlotTimeChangeQuartz.process() 시작 - MMSI: " + mmsiEntity.getMmsi() + 
				", 현재 slotTimeOut: " + mmsiEntity.getSlotTimeOut());
		
		if((this.mmsiEntity.getSlotTimeOut() - 1) <= -1) {
			//
			System.out.println("[DEBUG] slotTimeOut이 0 이하가 되어 초기화 시작 - MMSI: " + mmsiEntity.getMmsi());
			
			/**
			 * 트리거 제거
			 * 
			 */
			try {
				this.quartzCoreService.removeStartTimeTrigger(this.mmsiEntity);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * 다음 스케쥴을 위한 초기화
			 * GlobalEntityManager의 findSlotAndMarking에서 다음 스케쥴을 위한 setSlotTimeOutTime 설정
			 */
			this.mmsiEntity.setSlotTimeOutTime(null);
			
			int newSlotTimeOut;
			if(this.mmsiEntity.getSpeed() != 180) {
				newSlotTimeOut = RandomGenerator.generateRandomIntFromTo(0, 7);
			}else {
				newSlotTimeOut = 3;
			}
			System.out.println("[DEBUG] 새로운 slotTimeOut 설정: " + newSlotTimeOut + " - MMSI: " + mmsiEntity.getMmsi());
			this.mmsiEntity.setSlotTimeOut(newSlotTimeOut);

			/**
			 * 새로운 슬롯을 할당받기 전에 기존의 타겟 슬롯 정보 초기화
			 * 타겟 슬롯 초기화, 인덱스 초기화, NSS 초기화, 슛카운트 초기화
			 * SlotStateManager를 통해 점유한 슬롯 해제
			 */
			slotStateManager.releaseSlotsByMmsi(this.mmsiEntity.getMmsi());
			this.mmsiEntity.clearTargetSlotEntity();
			this.mmsiEntity.setnIndex(0);
			this.mmsiEntity.setNSS(this.mmsiEntity.getStartSlotNumber());
			this.mmsiEntity.clearShootCount(-1);
			
			/**
			 * 특정 시간에 모든 mmsi엔티티가 동시에 슬롯을 재할당 받는것을 방지하기 위해 랜덤 딜레이 부여(*추측)
			 * 
			 */
			int randomDelay = RandomGenerator.generateRandomIntFromTo(3, 10);
			this.mmsiEntity.setStartTime(this.mmsiEntity.getStartTime().plusSeconds(randomDelay));
			this.mmsiEntity.setPositionsCnt(0);
			this.mmsiEntity.setTargetChannel(true);
		}else {
			//
			if(this.mmsiEntity.getSpeed() != 180) {
				int currentSlotTimeOut = this.mmsiEntity.getSlotTimeOut();
				int newSlotTimeOut = currentSlotTimeOut - 1;
				System.out.println("[DEBUG] slotTimeOut 감소 - MMSI: " + mmsiEntity.getMmsi() + 
						", " + currentSlotTimeOut + " -> " + newSlotTimeOut);
				
				// slotTimeOut 감소
				this.mmsiEntity.setSlotTimeOut(newSlotTimeOut);
				
				// 다음 트리거를 위한 slotTimeOutTime 업데이트 (1분 후)
				if (this.mmsiEntity.getSlotTimeOutTime() != null) {
					this.mmsiEntity.setSlotTimeOutTime(this.mmsiEntity.getSlotTimeOutTime().plusMinutes(1));
					System.out.println("[DEBUG] slotTimeOutTime 업데이트 완료 - MMSI: " + mmsiEntity.getMmsi() + 
							", 새로운 시간: " + this.mmsiEntity.getSlotTimeOutTime());
				} else {
					System.out.println("[DEBUG] ⚠️ slotTimeOutTime이 null입니다 - MMSI: " + mmsiEntity.getMmsi());
				}
			} else {
				System.out.println("[DEBUG] Speed가 180이므로 slotTimeOut 감소하지 않음 - MMSI: " + mmsiEntity.getMmsi());
			}
		}
		
		System.out.println("[DEBUG] MmsiEntitySlotTimeChangeQuartz.process() 종료 - MMSI: " + mmsiEntity.getMmsi() + 
				", 최종 slotTimeOut: " + mmsiEntity.getSlotTimeOut());
	}
}




