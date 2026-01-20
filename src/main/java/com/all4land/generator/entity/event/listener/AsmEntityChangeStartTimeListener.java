package com.all4land.generator.entity.event.listener;

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

import com.all4land.generator.entity.AsmEntity;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.entity.event.AsmEntityChangeStartTimeEvent;
import com.all4land.generator.system.schedule.QuartzCoreService;

/**
 * Core 패키지의 AsmEntityChangeStartTimeEvent를 리스닝하는 리스너
 * ASM 메시지 생성 시작 시간이 변경될 때 Quartz Job을 등록합니다.
 */
@Component("coreAsmEntityChangeStartTimeListener")
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
		
		System.out.println("[DEBUG] MMSI: " + mmsiEntity.getMmsi() + ", ASM StartTime: " + newValue);
		
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
			e.printStackTrace();
		}
	}
}

