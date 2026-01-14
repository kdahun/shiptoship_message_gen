package com.all4land.generator.ui.tab.ais.entity.event.listener;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.entity.event.MmsiEntityChangeStartTimeEvent;

@Component
public class MmsiEntityChangeStartTimeListener {
	//
	private final QuartzCoreService quartzCoreService;
	private final Scheduler scheduler;

	MmsiEntityChangeStartTimeListener(QuartzCoreService quartzCoreService
			, Scheduler scheduler) {
		//
		this.quartzCoreService = quartzCoreService;
		this.scheduler = scheduler;
	}

	/**
	 * [MMSI_AIS_FLOW]-4
	 * Scheduler에 Job 등록을 위한 Trigger 생성
	 * To [MMSI_AIS_FLOW]-5 QuartzCoreService.addSchedulerJob
	 */
	@EventListener
	public void onMyPojoChange(MmsiEntityChangeStartTimeEvent event) {
		//
		System.out.println("[DEBUG] ========== MmsiEntityChangeStartTimeListener.onMyPojoChange() 호출 ==========");
		System.out.println("[DEBUG] 이벤트 수신 - 이벤트 타입: " + event.getClass().getName());
		String propertyName = event.getPropertyName();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		MmsiEntity mmsiEntity = (MmsiEntity) event.getClazz();
		System.out.println("[DEBUG] MmsiEntity 추출 - MMSI: " + (mmsiEntity != null ? mmsiEntity.getMmsi() : "null"));

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		String mmsi = String.valueOf(mmsiEntity.getMmsi());
		LocalDateTime localDateTime = mmsiEntity.getStartTime();
		
		String localDateTimeString = localDateTime.toString();
		
//		System.out.println(">>"+localDateTimeString);
		Trigger trigger = null;
		if(mmsiEntity.getJob() == null) {
			// Quartz Trigger 생성
			trigger = TriggerBuilder.newTrigger()
					.withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
		}else {
			// Quartz Trigger 생성
			trigger = TriggerBuilder.newTrigger()
					.forJob(mmsiEntity.getJob())
					.withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
		}
		
		try {
			System.out.println("[DEBUG] Quartz Job 등록 시도 - MMSI: " + mmsiEntity.getMmsi() + 
					", startTime: " + localDateTime);
			this.quartzCoreService.addScheduleJob(trigger, mmsiEntity);
			System.out.println("[DEBUG] Quartz Job 등록 완료 - MMSI: " + mmsiEntity.getMmsi());
		} catch (SchedulerException | ParseException e) {
			System.out.println("[DEBUG] ❌ Quartz Job 등록 실패 - MMSI: " + mmsiEntity.getMmsi());
			e.printStackTrace();
		}
		System.out.println("[DEBUG] ========== MmsiEntityChangeStartTimeListener.onMyPojoChange() 종료 ==========");
	}
}
