package com.all4land.generator.system.schedule.job;

import java.text.ParseException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.util.RandomGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MmsiEntitySlotTimeChangeQuartz implements Job {
	//
	private MmsiEntity mmsiEntity;
	private final QuartzCoreService quartzCoreService;
	
	MmsiEntitySlotTimeChangeQuartz(QuartzCoreService quartzCoreService){
		//
		this.quartzCoreService = quartzCoreService;
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
		if((this.mmsiEntity.getSlotTimeOut() - 1) <= -1) {
			//
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
			
			if(this.mmsiEntity.getSpeed() != 180) {
				this.mmsiEntity.setSlotTimeOut(RandomGenerator.generateRandomIntFromTo(0, 7));
			}else {
				this.mmsiEntity.setSlotTimeOut(3);
			}

			/**
			 * 새로운 슬롯을 할당받기 전에 기존의 타겟 슬롯 정보 초기화
			 * 타겟 슬롯 초기화, 인덱스 초기화, NSS 초기화, 슛카운트 초기화
			 */
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
				this.mmsiEntity.setSlotTimeOut(this.mmsiEntity.getSlotTimeOut()-1);
				this.mmsiEntity.setSlotTimeOutTime(this.mmsiEntity.getSlotTimeOutTime().plusMinutes(1));
			}
		}
	}
}




