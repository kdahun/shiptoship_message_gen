//package com.all4land.generator.system.schedule;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//
//import org.quartz.JobBuilder;
//import org.quartz.JobDataMap;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//import org.springframework.stereotype.Service;
//
//import com.all4land.generator.system.component.TimeMapRangeCompnents;
//import com.all4land.generator.system.constant.SystemConstMessage;
//import com.all4land.generator.ui.tab.ais.entity.GlobalSlotNumber;
//import com.all4land.generator.ui.tab.ais.entity.event.quartz.SlotNumberCalculateQuartz;
//
//@Service
//public class SlotNumberCalculateService {
//	//
//	private final Scheduler scheduler;
//	private final String group = "slotNumber";
//	private final GlobalSlotNumber globalSlotNumber;
//	private final TimeMapRangeCompnents timeMapRangeCompnents;
//
//	SlotNumberCalculateService(Scheduler scheduler, GlobalSlotNumber globalSlotNumber
//			, TimeMapRangeCompnents timeMapRangeCompnents) {
//		//
//		this.scheduler = scheduler;
//		this.globalSlotNumber = globalSlotNumber;
//		this.timeMapRangeCompnents = timeMapRangeCompnents;
//		
////		start();
//	}
//
//	public void start() {
//		//
//		LocalDateTime nowTime = LocalDateTime.now();
//		System.out.println("시작시간: "+nowTime);
//		
//		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS");
//		LocalDateTime startTime = nowTime.plusSeconds(1);
//		String ssSSSS = startTime.format(SystemConstMessage.formatterForStartIndex);
//		String header = startTime.format(SystemConstMessage.formatterSlotNumberHeader);
//		
//		double next_ssSSSS = timeMapRangeCompnents.findNextssSSSS(ssSSSS);
//		
//		String str = header+":"+String.format("%06.4f", next_ssSSSS);
//		System.out.println("str "+str);
//		LocalDateTime targetDate = LocalDateTime.parse(str, formatter);
//		
//		String localDateTimeString = targetDate.toString();
//
//		System.out.println("start"+localDateTimeString);
//		Trigger trigger = null;
//		if (this.globalSlotNumber.getSlotNumberJob() == null) {
//			// Quartz Trigger 생성
//			trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString, this.group)
//					.startAt(Date.from(targetDate.atZone(ZoneId.systemDefault()).toInstant())).build();
//		} else {
//			// Quartz Trigger 생성
//			trigger = TriggerBuilder.newTrigger().forJob(this.globalSlotNumber.getSlotNumberJob())
//					.withIdentity(localDateTimeString, this.group)
//					.startAt(Date.from(targetDate.atZone(ZoneId.systemDefault()).toInstant())).build();
//		}
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.put("time", targetDate);
//		// Quartz JobDetail 생성
//		try {
//			//
//			if (this.globalSlotNumber.getSlotNumberJob() == null) {
//				//
//				JobDetail job = JobBuilder.newJob(SlotNumberCalculateQuartz.class).withIdentity(localDateTimeString, this.group)
//						.storeDurably(true)
//						.setJobData(jobDataMap)
//						.build();
//				this.scheduler.scheduleJob(job, trigger);
//				this.globalSlotNumber.setSlotNumberJob(job);
//			} else {
//				//
//				// 트리거가 이미 존재하는지 확인
//				if (!this.scheduler.checkExists(trigger.getKey())) {
//					// 존재하지 않는 경우 스케줄에 트리거 추가
//					this.scheduler.scheduleJob(trigger);
//				}
//			}
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
//	}
//}
