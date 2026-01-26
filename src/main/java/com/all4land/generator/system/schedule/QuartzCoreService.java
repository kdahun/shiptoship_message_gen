package com.all4land.generator.system.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import com.all4land.generator.entity.AsmEntity;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.system.schedule.job.AsmEntityChangeStartDateQuartz;
import com.all4land.generator.system.schedule.job.MmsiEntityChangeStartDate;
import com.all4land.generator.system.schedule.job.MmsiEntitySlotTimeChangeQuartz;
import com.all4land.generator.system.schedule.job.VdeEntityChangeStartDateQuartz;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		System.out.println("[DEBUG] QuartzCoreService.addScheduleJob() 호출 - MMSI: " + mmsiEntity.getMmsi() + 
				", StartTime: " + mmsiEntity.getStartTime());
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getStartTime().toString();
			
			System.out.println("[DEBUG] 새 Quartz Job 생성 - MMSI: " + mmsi + ", StartDate: " + startDate);
			JobDetail job = JobBuilder.newJob(MmsiEntityChangeStartDate.class)
	                .withIdentity(startDate, mmsi) //"myJob", "group1"
	                .storeDurably(true)
	                .setJobData(jobDataMap)
	                .build();
			Date dt = this.scheduler.scheduleJob(job, trigger);
			// 현재시간 및 스케쥴된 시간 확인용 콘솔로그
			LocalTime now = LocalTime.now();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
			String time = sdf.format(dt);
			String curTime = now.format(fmt);
			System.out.println("[DEBUG] Quartz Job 스케줄 등록 완료 - MMSI: " + mmsiEntity.getMmsi());
			System.out.println("[Current   Time]" + curTime);
			System.out.println("[Scheduled Time]" + time);
			// 현재시간 및 스케쥴된 시간 확인용 콘솔로그
			mmsiEntity.setJob(job);
		}else {
			//
			//트리거가 이미 존재하는지 확인
			if (!this.scheduler.checkExists(trigger.getKey())) {
				// 존재하지 않는 경우 스케줄에 트리거 추가
				Date dt = this.scheduler.scheduleJob(trigger);
				// 현재시간 및 스케쥴된 시간 확인용 콘솔로그
				LocalTime now = LocalTime.now();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
				String time = sdf.format(dt);
				String curTime = now.format(fmt);
				System.out.println("[Current   Time]" + curTime);
				System.out.println("[Scheduled Time]" + time);
				// 현재시간 및 스케쥴된 시간 확인용 콘솔로그
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
	
	/**
	 * [SLOT_TIME_FLOW]-3
	 * MmsiEntitySlotTimeChangeQuartz Job 생성 및 트리거와 함께 스케쥴 등록
	 * 추후 트리거가 발동되면 MmsiEntitySlotTimeChangeQuartz.execute 실행
	 * @param trigger slotTimeOutTime 변경 시 발행되는 이벤트에서 생성한 트리거
	 * @param mmsiEntity 해당 스케쥴이 등록될 mmsiEntity 객체
	 * @throws SchedulerException 예외
	 * @throws ParseException 예외
	 */
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
		// 호환성을 위해 첫 번째 AsmEntity 사용
		addScheduleJobforAsm(trigger, mmsiEntity, mmsiEntity.getAsmEntity());
	}
	
	public void addScheduleJobforAsm(Trigger trigger, MmsiEntity mmsiEntity, AsmEntity asmEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		jobDataMap.put("asmEntity", asmEntity); // AsmEntity 직접 전달
		
		if(asmEntity.getAsmStartTimeJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String serviceId = asmEntity.getServiceId() != null ? asmEntity.getServiceId() : "default";
			String startDate = asmEntity.getStartTime().toString();
			
			// Job 키에 serviceId 포함 (각 AsmEntity별로 독립적인 Job)
			String jobKey = startDate + "_" + mmsi + "_" + serviceId;
			
			JobDetail job = JobBuilder.newJob(AsmEntityChangeStartDateQuartz.class)
	                .withIdentity(jobKey, mmsi) // Job 키에 serviceId 포함
	                .storeDurably(true)
	                .setJobData(jobDataMap)
	                .build();
			this.scheduler.scheduleJob(job, trigger);
			asmEntity.setAsmStartTimeJob(job);
			System.out.println("[DEBUG] ASM Job 생성 완료 - MMSI: " + mmsi + ", ServiceId: " + serviceId + ", JobKey: " + jobKey);
		}else {
			//
			//트리거가 이미 존재하는지 확인
			if (!this.scheduler.checkExists(trigger.getKey())) {
				// 존재하지 않는 경우 스케줄에 트리거 추가
				this.scheduler.scheduleJob(trigger);
			}
		}
		
    }
	
	public void removeAsmStartTimeTrigger(MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
		if(mmsiEntity.getAsmEntity().getAsmStartTimeJob() != null) {
			// 작업(Job)을 삭제하기 위해 해당 작업(Job)의 키를 얻어온다.
			JobKey jobKey = mmsiEntity.getAsmEntity().getAsmStartTimeJob().getKey();

			// 작업(Job)과 연결된 모든 트리거를 얻어온다.
			List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);

			// 작업(Job)과 연결된 모든 트리거를 삭제한다.
			for (Trigger trigger : triggersOfJob) {
				scheduler.unscheduleJob(trigger.getKey());
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

	// UI MmsiEntity를 받는 오버로드 메서드들
	public void addScheduleJob(Trigger trigger, com.all4land.generator.ui.tab.ais.entity.MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getStartTime().toString();
			
			JobDetail job = JobBuilder.newJob(com.all4land.generator.ui.tab.ais.entity.event.quartz.MmsiEntityChangeStartDate.class)
	                .withIdentity(startDate, mmsi) //"myJob", "group1"
	                .storeDurably(true)
	                .setJobData(jobDataMap)
	                .build();
			Date dt = this.scheduler.scheduleJob(job, trigger);
			// 현재시간 및 스케쥴된 시간 확인용 콘솔로그
			LocalTime now = LocalTime.now();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
			String time = sdf.format(dt);
			String curTime = now.format(fmt);
			System.out.println("[Current   Time]" + curTime);
			System.out.println("[Scheduled Time]" + time);
			// 현재시간 및 스케쥴된 시간 확인용 콘솔로그
			mmsiEntity.setJob(job);
		}else {
			//
			//트리거가 이미 존재하는지 확인
			if (!this.scheduler.checkExists(trigger.getKey())) {
				// 존재하지 않는 경우 스케줄에 트리거 추가
				Date dt = this.scheduler.scheduleJob(trigger);
				// 현재시간 및 스케쥴된 시간 확인용 콘솔로그
				LocalTime now = LocalTime.now();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
				String time = sdf.format(dt);
				String curTime = now.format(fmt);
				System.out.println("[Current   Time]" + curTime);
				System.out.println("[Scheduled Time]" + time);
				// 현재시간 및 스케쥴된 시간 확인용 콘솔로그
			}
		}
    }
	
	public void removeStartTimeTrigger(com.all4land.generator.ui.tab.ais.entity.MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
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
	
	public void removeSlotTimeOutTimeTrigger(com.all4land.generator.ui.tab.ais.entity.MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
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
	
	public void addScheduleJobForSlotTimeOut(Trigger trigger, com.all4land.generator.ui.tab.ais.entity.MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getSlotTimeOutJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getSlotTimeOutTime().toString();
			
			JobDetail job = JobBuilder.newJob(com.all4land.generator.ui.tab.ais.entity.event.quartz.MmsiEntitySlotTimeChangeQuartz.class)
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
	
	public void addScheduleJobforAsm(Trigger trigger, com.all4land.generator.ui.tab.ais.entity.MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getAsmEntity().getAsmStartTimeJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getAsmEntity().getStartTime().toString();
			
			JobDetail job = JobBuilder.newJob(com.all4land.generator.ui.tab.ais.entity.event.quartz.AsmEntityChangeStartDateQuartz.class)
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
	
	public void addScheduleJobforVde(Trigger trigger, com.all4land.generator.ui.tab.ais.entity.MmsiEntity mmsiEntity) throws SchedulerException, ParseException {
		//
        // Quartz JobDetail 생성
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		if(mmsiEntity.getVdeEntity().getVdeStartTimeJob() == null) {
			//
			String mmsi = String.valueOf(mmsiEntity.getMmsi());
			String startDate = mmsiEntity.getVdeEntity().getStartTime().toString();
			
			JobDetail job = JobBuilder.newJob(com.all4land.generator.ui.tab.ais.entity.event.quartz.VdeEntityChangeStartDateQuartz.class)
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
