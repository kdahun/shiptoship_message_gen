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
import com.all4land.generator.ui.tab.ais.entity.event.change.MmsiEntitySlotTimeChangeEvent;

@Component
public class MmsiEntitySlotTimeChangeListener {
	//
	private final QuartzCoreService quartzCoreService;
	private final Scheduler scheduler;

	MmsiEntitySlotTimeChangeListener(QuartzCoreService quartzCoreService
			, Scheduler scheduler) {
		//
		this.quartzCoreService = quartzCoreService;
		this.scheduler = scheduler;
	}

	/**
	 * [SLOT_TIME_FLOW]-2
	 * MmsiEntity slotTimeOutTime 변경 시 발행되는 이벤트
	 * 트리거 생성 및 QuartzCoreService 스케쥴을 위한 Job 생성 함수 호출
	 * 트리거 발동 시간 = (MmsiEntity에서 변경된 LocalDateTime slotTimeOutTime : Object newValue)
	 * @param event Object source, String propertyName, Object oldValue, Object newValue, Object clazz, GlobalEntityManager globalEntityManager
	*/
	@EventListener
	public void onChange(MmsiEntitySlotTimeChangeEvent event) {
		//
		String propertyName = event.getPropertyName();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		MmsiEntity mmsiEntity = (MmsiEntity) event.getClazz();

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		jobDataMap.put("globalEntityManager", event.getGlobalEntityManager());
		
		String mmsi = String.valueOf(mmsiEntity.getMmsi());
		LocalDateTime localDateTime = (LocalDateTime) newValue;
		
		if(newValue != null) {
			String localDateTimeString = localDateTime.toString();
			Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			
			Trigger trigger = null;
			if(mmsiEntity.getSlotTimeOutJob() == null) {
				// Quartz Trigger 생성
				trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString, mmsi)
						.startAt(date)
						.build();
			}else {
				// Quartz Trigger 생성
				trigger = TriggerBuilder.newTrigger()
						.forJob(mmsiEntity.getSlotTimeOutJob())
						.withIdentity(localDateTimeString, mmsi)
						.startAt(date)
						.build();
			}
			
			try {
				this.quartzCoreService.addScheduleJobForSlotTimeOut(trigger, mmsiEntity);
			} catch (SchedulerException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
