package com.all4land.generator.system.schedule;

import java.text.ParseException;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.ui.tab.ais.entity.event.quartz.AsmEntityChangeStartDateQuartz;
import com.all4land.generator.ui.tab.ais.entity.event.quartz.MmsiEntityChangeStartDate;
import com.all4land.generator.ui.tab.ais.entity.event.quartz.MmsiEntitySlotTimeChangeQuartz;
import com.all4land.generator.ui.tab.ais.entity.event.quartz.VdeEntityChangeStartDateQuartz;

@Service
public class QuartzCoreService {
	//
	private final Scheduler scheduler;
	
	@SuppressWarnings("unused")
	QuartzCoreService(Scheduler scheduler) {
		//
		this.scheduler = scheduler;
	}
	
	/**
	 * [MMSI_AIS_FLOW]-5
	 * MmsiEntity에 Job이 없을 경우 Job 생성(MmsiEntityChangeStartDate.class implements Job)
	 * Job이 있을 경우 트리거만 추가
	 * To [MMSI_AIS_FLOW]-6 MmsiEntityChangeStartDate.execute @Override (트리거 활성화 시 자동으로 호출)
	 */
	public void addScheduleJob(Trigger trigger, MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getStartTime().toString();
			
			JobDetail job = JobBuilder.newJob(MmsiEntityChangeStartDate.class)
	                .withIdentity(startDate, mmsi) //"myJob", "group1"
	                .storeDurably(true)
	                .setJobData(jobDataMap)
	                .build();
			this.scheduler.scheduleJob(job, trigger);
			mmsiEntity.setJob(job);
		}else {
			//
			//트리거가 이미 존재하는지 확인
			if (!this.scheduler.checkExists(trigger.getKey())) {
				// 존재하지 않는 경우 스케줄에 트리거 추가
				this.scheduler.scheduleJob(trigger);
			}
		}
		
    }
	
	public void removeStartTimeTrigger(MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
		if(mmsiEntity.getJob() != null) {
			// 작업(Job)을 삭제하기 위해 해당 작업(Job)의 키를 얻어온다.
			JobKey jobKey = mmsiEntity.getJob().getKey();

			// 작업(Job)과 연결된 모든 트리거를 얻어온다.
			List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);

			// 작업(Job)과 연결된 모든 트리거를 삭제한다.
			for (Trigger trigger : triggersOfJob) {
				scheduler.unscheduleJob(trigger.getKey());
//				System.out.println(rtn+", "+trigger.getKey().getName());
			}
		}
    }
	
	public void removeSlotTimeOutTimeTrigger(MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
		if(mmsiEntity.getSlotTimeOutJob() != null) {
			// 작업(Job)을 삭제하기 위해 해당 작업(Job)의 키를 얻어온다.
			JobKey jobKey = mmsiEntity.getSlotTimeOutJob().getKey();

			// 작업(Job)과 연결된 모든 트리거를 얻어온다.
			List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);

			// 작업(Job)과 연결된 모든 트리거를 삭제한다.
			for (Trigger trigger : triggersOfJob) {
				scheduler.unscheduleJob(trigger.getKey());
//				System.out.println(rtn+", "+trigger.getKey().getName());
			}
		}
    }
	
	public void addScheduleJobForSlotTimeOut(Trigger trigger, MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getSlotTimeOutJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getSlotTimeOutTime().toString();
			
			JobDetail job = JobBuilder.newJob(MmsiEntitySlotTimeChangeQuartz.class)
	                .withIdentity(startDate, mmsi)
	                .storeDurably(true)
	                .setJobData(jobDataMap)
	                .build();
			this.scheduler.scheduleJob(job, trigger);
			mmsiEntity.setSlotTimeOutJob(job);
		}else {
			//
			//트리거가 이미 존재하는지 확인
			if (!this.scheduler.checkExists(trigger.getKey())) {
				// 존재하지 않는 경우 스케줄에 트리거 추가
				this.scheduler.scheduleJob(trigger);
			}
		}
		
    }
	
	public void addScheduleJobforAsm(Trigger trigger, MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getAsmEntity().getAsmStartTimeJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getAsmEntity().getStartTime().toString();
			
			JobDetail job = JobBuilder.newJob(AsmEntityChangeStartDateQuartz.class)
	                .withIdentity(startDate, mmsi) //"myJob", "group1"
	                .storeDurably(true)
	                .setJobData(jobDataMap)
	                .build();
			this.scheduler.scheduleJob(job, trigger);
			mmsiEntity.getAsmEntity().setAsmStartTimeJob(job);
		}else {
			//
			//트리거가 이미 존재하는지 확인
			if (!this.scheduler.checkExists(trigger.getKey())) {
				// 존재하지 않는 경우 스케줄에 트리거 추가
				this.scheduler.scheduleJob(trigger);
			}
		}
		
    }
	
	public void addScheduleJobforVde(Trigger trigger, MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getVdeEntity().getVdeStartTimeJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getVdeEntity().getStartTime().toString();
			
			JobDetail job = JobBuilder.newJob(VdeEntityChangeStartDateQuartz.class)
	                .withIdentity(startDate, mmsi) //"myJob", "group1"
	                .storeDurably(true)
	                .setJobData(jobDataMap)
	                .build();
			this.scheduler.scheduleJob(job, trigger);
			mmsiEntity.getVdeEntity().setVdeStartTimeJob(job);
		}else {
			//
			//트리거가 이미 존재하는지 확인
			if (!this.scheduler.checkExists(trigger.getKey())) {
				// 존재하지 않는 경우 스케줄에 트리거 추가
				this.scheduler.scheduleJob(trigger);
			}
		}
		
    }
	
	
}
