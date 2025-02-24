package com.nsone.generator.ui.tab.ais.entity.event.listener;

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

import com.nsone.generator.system.schedule.QuartzCoreService;
import com.nsone.generator.ui.tab.ais.entity.AsmEntity;
import com.nsone.generator.ui.tab.ais.entity.MmsiEntity;
import com.nsone.generator.ui.tab.ais.entity.event.change.AsmEntityChangeStartTimeEvent;
import com.nsone.generator.ui.tab.ais.entity.event.change.MmsiEntityChangeStartTimeEvent;

@Component
public class AsmEntityChangeStartTimeListener {
	//
	private final QuartzCoreService quartzCoreService;

	AsmEntityChangeStartTimeListener(QuartzCoreService quartzCoreService) {
		//
		this.quartzCoreService = quartzCoreService;
	}

	@EventListener
	public void onChange(AsmEntityChangeStartTimeEvent event) {
		//
		LocalDateTime oldValue = event.getOldValue();
		LocalDateTime newValue = event.getNewValue();
		MmsiEntity mmsiEntity = event.getMmsiEntity();
		AsmEntity asmEntity = event.getAsmEntity();
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);

		String mmsi = String.valueOf(mmsiEntity.getMmsi());
		LocalDateTime localDateTime = newValue;

		String localDateTimeString = localDateTime.toString();

		Trigger trigger = null;
		if (asmEntity.getAsmStartTimeJob() == null) {
			// Quartz Trigger 생성
			trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
		} else {
			// Quartz Trigger 생성
			trigger = TriggerBuilder.newTrigger().forJob(asmEntity.getAsmStartTimeJob()).withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())).build();
		}

		try {
			this.quartzCoreService.addScheduleJobforAsm(trigger, mmsiEntity);
		} catch (SchedulerException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
