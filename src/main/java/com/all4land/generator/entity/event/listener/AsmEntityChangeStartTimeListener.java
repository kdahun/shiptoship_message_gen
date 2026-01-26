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
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.schedule.QuartzCoreService;

/**
 * Core 패키지의 AsmEntityChangeStartTimeEvent를 리스닝하는 리스너
 * ASM 메시지 생성 시작 시간이 변경될 때 Quartz Job을 등록합니다.
 * 배속(VirtualTimeManager)을 적용하여 가상 시간을 실제 시간으로 변환합니다.
 */
@Component("coreAsmEntityChangeStartTimeListener")
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
		
		String serviceId = asmEntity.getServiceId() != null ? asmEntity.getServiceId() : "default";
		System.out.println("[DEBUG] MMSI: " + mmsiEntity.getMmsi() + ", ServiceId: " + serviceId + ", ASM StartTime: " + newValue);

		String mmsi = String.valueOf(mmsiEntity.getMmsi());
		LocalDateTime localDateTime = newValue; // 가상 시간
		
		// 가상 시간을 실제 시간으로 변환 (Quartz는 실제 시간을 사용)
		LocalDateTime realDateTime = virtualTimeManager.convertVirtualToRealTime(localDateTime);

		String localDateTimeString = localDateTime.toString();
		// Trigger 키에 serviceId 포함 (각 AsmEntity별로 독립적인 Trigger)
		String triggerKey = localDateTimeString + "_" + mmsi + "_" + serviceId;
		
		System.out.println("[DEBUG] ASM 가상 시간: " + localDateTime + " -> 실제 시간: " + realDateTime + ", ServiceId: " + serviceId);

		Trigger trigger = null;
		if (asmEntity.getAsmStartTimeJob() == null) {
			// Quartz Trigger 생성 (실제 시간 사용)
			trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey, mmsi)
					.startAt(Date.from(realDateTime.atZone(ZoneId.systemDefault()).toInstant()))
					.build();
		} else {
			// Quartz Trigger 생성 (실제 시간 사용)
			trigger = TriggerBuilder.newTrigger().forJob(asmEntity.getAsmStartTimeJob()).withIdentity(triggerKey, mmsi)
					.startAt(Date.from(realDateTime.atZone(ZoneId.systemDefault()).toInstant())).build();
		}

		try {
			this.quartzCoreService.addScheduleJobforAsm(trigger, mmsiEntity, asmEntity);
		} catch (SchedulerException | ParseException e) {
			e.printStackTrace();
		}
	}
}

