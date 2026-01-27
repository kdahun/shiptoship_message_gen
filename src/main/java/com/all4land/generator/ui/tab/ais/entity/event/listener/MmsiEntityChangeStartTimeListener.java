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

import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.entity.event.MmsiEntityChangeStartTimeEvent;

@Component
public class MmsiEntityChangeStartTimeListener {
	//
	private final QuartzCoreService quartzCoreService;
	private final Scheduler scheduler;
	private final VirtualTimeManager virtualTimeManager;

	MmsiEntityChangeStartTimeListener(QuartzCoreService quartzCoreService
			, Scheduler scheduler
			, VirtualTimeManager virtualTimeManager) {
		//
		this.quartzCoreService = quartzCoreService;
		this.scheduler = scheduler;
		this.virtualTimeManager = virtualTimeManager;
	}

	/**
	 * [MMSI_AIS_FLOW]-4
	 * Scheduler에 Job 등록을 위한 Trigger 생성
	 * To [MMSI_AIS_FLOW]-5 QuartzCoreService.addSchedulerJob
	 */
	@EventListener
	public void onMyPojoChange(MmsiEntityChangeStartTimeEvent event) {
		//
		String propertyName = event.getPropertyName();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		MmsiEntity mmsiEntity = (MmsiEntity) event.getClazz();

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		String mmsi = String.valueOf(mmsiEntity.getMmsi());
		LocalDateTime localDateTime = mmsiEntity.getStartTime(); // 가상 시간
		
		// 가상 시간을 실제 시간으로 변환 (Quartz는 실제 시간을 사용)
		LocalDateTime realDateTime = virtualTimeManager.convertVirtualToRealTime(localDateTime);
		
		String localDateTimeString = localDateTime.toString();
		
		Trigger trigger = null;
		if(mmsiEntity.getJob() == null) {
			// Quartz Trigger 생성 (실제 시간 사용)
			trigger = TriggerBuilder.newTrigger()
					.withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(realDateTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
		}else {
			// Quartz Trigger 생성 (실제 시간 사용)
			trigger = TriggerBuilder.newTrigger()
					.forJob(mmsiEntity.getJob())
					.withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(realDateTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
		}
		
		try {
			this.quartzCoreService.addScheduleJob(trigger, mmsiEntity);
		} catch (SchedulerException | ParseException e) {
			System.out.println("[DEBUG] ❌ Quartz Job 등록 실패 - MMSI: " + mmsiEntity.getMmsi());
			e.printStackTrace();
		}
	}
}
