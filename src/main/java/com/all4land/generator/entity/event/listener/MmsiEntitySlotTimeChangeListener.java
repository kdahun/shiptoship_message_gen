package com.all4land.generator.entity.event.listener;

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

import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.entity.event.MmsiEntitySlotTimeChangeEvent;
import com.all4land.generator.system.schedule.QuartzCoreService;

/**
 * Core 패키지의 MmsiEntitySlotTimeChangeEvent를 리스닝하는 리스너
 * slotTimeOutTime이 변경될 때 다음 트리거를 생성합니다.
 */
@Component("coreMmsiEntitySlotTimeChangeListener")
public class MmsiEntitySlotTimeChangeListener {
	//
	private final QuartzCoreService quartzCoreService;
	private final Scheduler scheduler;

	MmsiEntitySlotTimeChangeListener(QuartzCoreService quartzCoreService, Scheduler scheduler) {
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
		Object newValue = event.getNewValue();
		MmsiEntity mmsiEntity = (MmsiEntity) event.getClazz();
		
		System.out.println("[DEBUG] MmsiEntitySlotTimeChangeListener.onChange() 호출 - MMSI: " + mmsiEntity.getMmsi() + 
				", propertyName: " + propertyName + ", newValue: " + newValue);

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("mmsiEntity", mmsiEntity);
		
		String mmsi = String.valueOf(mmsiEntity.getMmsi());
		LocalDateTime localDateTime = (LocalDateTime) newValue;
		
		if(newValue != null) {
			String localDateTimeString = localDateTime.toString();
			Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			
			System.out.println("[DEBUG] 다음 트리거 생성 시도 - MMSI: " + mmsiEntity.getMmsi() + 
					", triggerTime: " + localDateTimeString);
			
			Trigger trigger;
			if(mmsiEntity.getSlotTimeOutJob() == null) {
				// Quartz Trigger 생성
				trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString, mmsi)
						.startAt(date)
						.build();
				System.out.println("[DEBUG] 새로운 Job을 위한 Trigger 생성 - MMSI: " + mmsiEntity.getMmsi());
			}else {
				// Quartz Trigger 생성
				trigger = TriggerBuilder.newTrigger()
						.forJob(mmsiEntity.getSlotTimeOutJob())
						.withIdentity(localDateTimeString, mmsi)
						.startAt(date)
						.build();
				System.out.println("[DEBUG] 기존 Job을 위한 Trigger 생성 - MMSI: " + mmsiEntity.getMmsi());
			}
			
			try {
				this.quartzCoreService.addScheduleJobForSlotTimeOut(trigger, mmsiEntity);
				System.out.println("[DEBUG] ✅ 다음 트리거 등록 완료 - MMSI: " + mmsiEntity.getMmsi() + 
						", triggerTime: " + localDateTimeString);
			} catch (SchedulerException | ParseException e) {
				System.out.println("[DEBUG] ❌ 트리거 등록 실패 - MMSI: " + mmsiEntity.getMmsi() + ", 오류: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("[DEBUG] ⚠️ newValue가 null이므로 트리거 생성하지 않음 - MMSI: " + mmsiEntity.getMmsi());
		}
	}
}

