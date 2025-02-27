package com.all4land.generator.ui.tab.ais.entity.event.listener;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.ui.tab.ais.entity.VdeEntity;
import com.all4land.generator.ui.tab.ais.entity.event.change.VdeEntityChangeStartTimeEvent;

@Component
public class VdeEntityChangeStartTimeListener {
	//
	private final QuartzCoreService quartzCoreService;

	VdeEntityChangeStartTimeListener(QuartzCoreService quartzCoreService) {
		//
		this.quartzCoreService = quartzCoreService;
	}

	@EventListener
	public void onChange(VdeEntityChangeStartTimeEvent event) {
		//
		LocalDateTime oldValue = event.getOldValue();
		LocalDateTime newValue = event.getNewValue();
		MmsiEntity mmsiEntity = event.getMmsiEntity();
		VdeEntity vdeEntity = event.getVdeEntity();
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);

		String mmsi = String.valueOf(mmsiEntity.getMmsi());
		LocalDateTime localDateTime = newValue;

		String localDateTimeString = localDateTime.toString();

		Trigger trigger = null;
		if (vdeEntity.getVdeStartTimeJob() == null) {
			// Quartz Trigger 생성
			trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
		} else {
			// Quartz Trigger 생성
			trigger = TriggerBuilder.newTrigger().forJob(vdeEntity.getVdeStartTimeJob()).withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())).build();
		}

		try {
			this.quartzCoreService.addScheduleJobforVde(trigger, mmsiEntity);
		} catch (SchedulerException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
