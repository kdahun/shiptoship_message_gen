package com.nsone.generator.ui.tab.ais.entity.event.quartz;

import java.text.ParseException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nsone.generator.system.schedule.QuartzCoreService;
import com.nsone.generator.ui.tab.ais.entity.MmsiEntity;
import com.nsone.generator.util.RandomGenerator;

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
	
//	@Async
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		this.mmsiEntity = (MmsiEntity) jobDataMap.get("mmsiEntity");
		
		this.process();
	}

	private void process() {
		//
		if((this.mmsiEntity.getSlotTimeOut() - 1) <= -1) {
			//
			
			try {
				this.quartzCoreService.removeStartTimeTrigger(this.mmsiEntity);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.mmsiEntity.setSlotTimeOutTime(null);
			
			if(this.mmsiEntity.getSpeed() != 180) {
//				this.mmsiEntity.setSlotTimeOut(this.mmsiEntity.getSlotTimeOut_default());
				this.mmsiEntity.setSlotTimeOut(RandomGenerator.generateRandomIntFromTo(0, 7));
//				this.mmsiEntity.setSlotTimeOut(7); // ===== 테스트용도
			}else {
				this.mmsiEntity.setSlotTimeOut(3);
			}
			this.mmsiEntity.clearTargetSlotEntity();
//			this.mmsiEntity.setCalculateBasic();
//			this.mmsiEntity.setNSSA(-1);
			this.mmsiEntity.setnIndex(0);
			this.mmsiEntity.setNSS(this.mmsiEntity.getStartSlotNumber());
			this.mmsiEntity.clearShootCount(-1);
//			this.mmsiEntity.setStartTime(this.mmsiEntity.getStartTime().plusMinutes(1));
			
			int randomDelay = RandomGenerator.generateRandomIntFromTo(3, 10);
			this.mmsiEntity.setStartTime(this.mmsiEntity.getStartTime().plusSeconds(randomDelay)); //====== 10초후 다시 진행
			this.mmsiEntity.setPositionsCnt(0); //============================================
			this.mmsiEntity.setTargetChannel(true);
//			System.out.println("끝 setSlotTimeOutTime : "+LocalDateTime.now());
//			if(this.mmsiEntity.getSpeed() != 180) {
//				this.mmsiEntity.setnIndex(0);
//				
//				this.mmsiEntity.clearTargetSlotEntity();
//				this.mmsiEntity.setStartTime(this.mmsiEntity.getStartTime().plusMinutes(1)); 
//				this.mmsiEntity.setSlotTimeOut(RandomGenerator.generateRandomIntFromTo(0, 7));
//				this.mmsiEntity.setSlotTimeOutTime(null);
//				this.mmsiEntity.setTargetChannel(true);
//				log.info("완전종료 mmsi : {}", this.mmsiEntity);
//			}else {
//				this.mmsiEntity.setSlotTimeOut(3);
//			}
			
			
//			System.out.println("끝 새로시작할시간 : "+this.mmsiEntity.getStartTime());
		}else {
			//
			if(this.mmsiEntity.getSpeed() != 180) {
				this.mmsiEntity.setSlotTimeOut(this.mmsiEntity.getSlotTimeOut()-1);
				this.mmsiEntity.setSlotTimeOutTime(this.mmsiEntity.getSlotTimeOutTime().plusMinutes(1));
			}
			//else {
//				
//			}
			
//			System.out.println("변경 setSlotTimeOutTime : "+this.mmsiEntity.getSlotTimeOutTime());
		}
	}
	
}
