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
import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.ui.tab.ais.entity.event.change.MmsiEntityChangeStartTimeEvent;

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
			this.quartzCoreService.addScheduleJob(trigger, mmsiEntity);
		} catch (SchedulerException | ParseException e) {
			e.printStackTrace();
		}
		
	}
}
