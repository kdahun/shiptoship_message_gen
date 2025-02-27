//package com.all4land.generator.ui.tab.ais.entity.event.quartz;
//
//import java.time.LocalDateTime;
//import java.util.concurrent.CompletableFuture;
//
//import org.quartz.Job;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.stereotype.Component;
//
//import com.all4land.generator.system.component.TimeMapRangeCompnents;
//import com.all4land.generator.system.constant.SystemConstMessage;
//import com.all4land.generator.ui.service.ResourceService;
//import com.all4land.generator.ui.tab.ais.entity.GlobalSlotNumber;
//import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class SlotNumberCalculateQuartz implements Job {
//	//
//	private final GlobalSlotNumber globalSlotNumber;
//	private final TimeMapRangeCompnents timeMapRangeCompnents;
//	private final ResourceService resourceService;
//	
//	public SlotNumberCalculateQuartz(TimeMapRangeCompnents timeMapRangeCompnents
//			, GlobalSlotNumber globalSlotNumber, ResourceService resourceService){
//		//
//		this.timeMapRangeCompnents = timeMapRangeCompnents;
//		this.globalSlotNumber = globalSlotNumber;
//		this.resourceService = resourceService;
//	}
//	
//	@Override
//	public void execute(JobExecutionContext context) throws JobExecutionException {
//		// TODO Auto-generated method stub
//		System.out.println("스케쥴 시작1 : " + LocalDateTime.now());
////		JobDataMap jobDataMap = context.getMergedJobDataMap();
////		reservationTime = (LocalDateTime) jobDataMap.get("time");
////		this.process();
//		CompletableFuture.runAsync(() -> this.process());
//	}
//
//	private void process() {
//		//
//		System.out.println("스케쥴 시작2 : " + LocalDateTime.now());
//		
//		this.timeMapRangeCompnents.findStartSlotNumber(LocalDateTime.now().format(SystemConstMessage.formatterForStartIndex));
//		
//		
//		this.globalSlotNumber.setSlotNumber(this.timeMapRangeCompnents.findStartSlotNumber(LocalDateTime.now().format(SystemConstMessage.formatterForStartIndex)));
//		
//		int slotNumber = this.globalSlotNumber.getSlotNumber();
//		
//		if(slotNumber >= 0 && slotNumber <= 5 || slotNumber >= 2245 && slotNumber <= 2249) {
//			System.out.println("시작시간 : "+ LocalDateTime.now()+", 현재 슬롯: "+slotNumber);
//		}
////		else {
////			System.out.println("시작시간 : "+ LocalDateTime.now()+", 현재 슬롯: "+slotNumber);
////		}
//		
//		
//		if(slotNumber == 18) {
//			//
//			System.out.println("지금 요청한다.");
//			this.resourceService.resourceStart(1, 1, String.valueOf(slotNumber));
//		}
//	}
//}
