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

import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.ui.tab.ais.entity.AsmEntity;
import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.ui.tab.ais.entity.event.change.AsmEntityChangeStartTimeEvent;

@Component
public class AsmEntityChangeStartTimeListener {
	//
	private final QuartzCoreService quartzCoreService;
	private final VirtualTimeManager virtualTimeManager;

	AsmEntityChangeStartTimeListener(QuartzCoreService quartzCoreService
			, VirtualTimeManager virtualTimeManager) {
		//
		this.quartzCoreService = quartzCoreService;
		this.virtualTimeManager = virtualTimeManager;
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
		LocalDateTime localDateTime = newValue; // 가상 시간

		// 가상 시간을 실제 시간으로 변환 (Quartz는 실제 시간을 사용)
		LocalDateTime realDateTime = virtualTimeManager.convertVirtualToRealTime(localDateTime);

		String localDateTimeString = localDateTime.toString();
		
		System.out.println("[DEBUG] ASM 가상 시간: " + localDateTime + " -> 실제 시간: " + realDateTime);

		Trigger trigger = null;
		if (asmEntity.getAsmStartTimeJob() == null) {
			// Quartz Trigger 생성 (실제 시간 사용)
			trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(realDateTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
		} else {
			// Quartz Trigger 생성 (실제 시간 사용)
			trigger = TriggerBuilder.newTrigger().forJob(asmEntity.getAsmStartTimeJob()).withIdentity(localDateTimeString, mmsi)
					.startAt(Date.from(realDateTime.atZone(ZoneId.systemDefault()).toInstant())).build();
		}

		try {
			this.quartzCoreService.addScheduleJobforAsm(trigger, mmsiEntity);
		} catch (SchedulerException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
