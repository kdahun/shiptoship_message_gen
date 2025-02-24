package com.nsone.generator.ui.tab.ais.entity.event.listener;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nsone.generator.system.constant.SystemConstMessage;
import com.nsone.generator.ui.service.ResourceService;
import com.nsone.generator.ui.tab.ais.entity.GlobalEntityManager;
import com.nsone.generator.ui.tab.ais.entity.GlobalSlotNumber;
import com.nsone.generator.ui.tab.ais.entity.event.change.ToggleSlotNumberEvent;
import com.nsone.generator.ui.view.Sample1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ToggleSlotNumberListener {
	//
	private final Sample1 mainFrame;
    private final GlobalSlotNumber globalSlotNumber;
    private final ResourceService resourceService;
    private final GlobalEntityManager globalEntityManager;

    public ToggleSlotNumberListener(GlobalSlotNumber globalSlotNumber
    		, ResourceService resourceService, Sample1 mainFrame
    		, GlobalEntityManager globalEntityManager) {
        //
        this.globalSlotNumber = globalSlotNumber;
        this.resourceService = resourceService;
        this.mainFrame = mainFrame;
        this.globalEntityManager = globalEntityManager;
    }

    @EventListener
    public void onListener(ToggleSlotNumberEvent event) {
    	//
    	CompletableFuture.runAsync(() -> this.tsqMessageStart());
    }

    private void tsqMessageStart() {
    	//
    	int slotNumber = this.globalSlotNumber.getSlotNumber();
    	
    	long mmsi = Long.MIN_VALUE;
    	String mmsiStr = "";
    	try {
			mmsi = globalEntityManager.getMmsiEntityLists().get(0).getMmsi();
			mmsiStr = String.valueOf(mmsi);
		}catch(Exception e) {
//			log.info("자체 방송용이다");
		}
    	
    	if("".equals(mmsiStr)) { // 방송용
//    		log.info("자체 방송용이다");
    		if(SystemConstMessage.TSQ_TEST_SLOT_NUMBER_1.contains(slotNumber)) {
    			if(this.mainFrame.getTSQTestMode()) {
        			this.resourceService.resourceStart(0, 0, String.valueOf(slotNumber));
        		}
    		}
    	}else {
    		if(SystemConstMessage.TSQ_TEST_SLOT_NUMBER_ALL2.contains(slotNumber)) {
    			if(this.mainFrame.getTSQTestMode()) {
        			this.resourceService.resourceStart(0, 0, String.valueOf(slotNumber));
        		}
    		}
    	}
    	
//    	if(SystemConstMessage.TSQ_TEST_SLOT_NUMBER_0.contains(slotNumber)) {
//    	if(slotNumber == 210 || slotNumber == 618 || slotNumber == 1236 ||slotNumber == 1818 || slotNumber == 2214) {
    		//
//    		System.out.println("지금 요청한다. slotNumber : " + slotNumber+ ", start Time : "+ LocalDateTime.now());
    		
//    		if(this.mainFrame.getTSQTestMode()) {
//    			this.resourceService.resourceStart(0, 0, String.valueOf(slotNumber));
//    		}
    		
//    	}
    }
    
    public Sample1 getMainFrame() {
		return mainFrame;
	}

//	public void setMainFrame(Sample1 mainFrame) {
//		this.mainFrame = mainFrame;
//	}
}

//@Component
//public class ToggleSlotNumberListener {
//	//
//	private final Scheduler scheduler;
//	private final TimeMapRangeCompnents timeMapRangeCompnents;
//	private final String group = "slotNumber";
//	private final GlobalSlotNumber globalSlotNumber;
//	
//	public ToggleSlotNumberListener(Scheduler scheduler, TimeMapRangeCompnents timeMapRangeCompnents, GlobalSlotNumber globalSlotNumber) {
//		//
//		this.scheduler = scheduler;
//		this.timeMapRangeCompnents = timeMapRangeCompnents;
//		this.globalSlotNumber = globalSlotNumber;
//	}
//	
//	@EventListener
//	public void onListener(ToggleSlotNumberEvent event) {
//		//
//		if(event.isMode()) {
//			// 미래를 등록
//			this.continueMode(event);
//		} else {
//			// start mode
//			this.startMode(event);
//		}
//		
//		
//	}
//	
//	private void startMode(ToggleSlotNumberEvent event) {
//		//
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS");
//		LocalDateTime startTime = LocalDateTime.now().plusSeconds(1);
//
//		String ssSSSS = startTime.format(SystemConstMessage.formatterForStartIndex);
//		String header = startTime.format(SystemConstMessage.formatterSlotNumberHeader);
//		
//		double next_ssSSSS = timeMapRangeCompnents.findNextssSSSS(ssSSSS);
//		
//		String str = "";
//		if(next_ssSSSS < 10) {
//			str = header+":0"+String.format("%06.4f", next_ssSSSS);
//		}else {
//			str = header+":"+String.format("%06.4f", next_ssSSSS);
//		}
//		
//		//System.out.println("str "+str);
//		LocalDateTime targetDate = LocalDateTime.parse(str, formatter);
//		
//		String localDateTimeString = targetDate.toString();
//		//System.out.println("future"+localDateTimeString);
//		
//		Trigger trigger = null;
//		if (this.globalSlotNumber.getSlotNumberJob() == null) {
//			// Quartz Trigger 생성
//			trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString+"slotNumberJob", this.group)
//					.startAt(Date.from(targetDate.atZone(ZoneId.systemDefault()).toInstant())).build();
//		} else {
//			// Quartz Trigger 생성
//			trigger = TriggerBuilder.newTrigger().forJob(this.globalSlotNumber.getSlotNumberJob())
//					.withIdentity(localDateTimeString+"slotNumberJob", this.group)
//					.startAt(Date.from(targetDate.atZone(ZoneId.systemDefault()).toInstant())).build();
//		}
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.put("time", targetDate);
//		//System.out.println("보낼시간: "+targetDate);
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
//					//System.out.println("트리거추가");
//				}
//			}
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
//	
//	private void continueMode(ToggleSlotNumberEvent event) {
//		//
//		//System.out.println("continueMode ====================");
//		//System.out.println("시작시간 : "+ LocalDateTime.now());
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS");
//		
//		int slotNumber = this.globalSlotNumber.getSlotNumber();
//		//System.out.println("slotNumber : "+ slotNumber);
//		
//		LocalDateTime startTime = LocalDateTime.now();
//
//		String header = startTime.format(SystemConstMessage.formatterSlotNumberHeader);
//		
//		double next_ssSSSS = timeMapRangeCompnents.getNextssSSSS(slotNumber);
//		//System.out.println(ssSSSS+", "+next_ssSSSS);
//		
//		if(slotNumber >= 2249) {
//			//
//			//System.out.println("넘아가야한다");
//			startTime = startTime.plusMinutes(1);
//			header = startTime.format(SystemConstMessage.formatterSlotNumberHeader);
//			
//			String str = header+":00.0000";
//			
//			//System.out.println("str "+str);
//			LocalDateTime targetDate = LocalDateTime.parse(str, formatter);
//			
//			String localDateTimeString = targetDate.toString();
//			//System.out.println("future"+localDateTimeString);
//			
//			Trigger trigger = null;
//			if (this.globalSlotNumber.getSlotNumberJob() == null) {
//				// Quartz Trigger 생성
//				trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString+"slotNumberJob", this.group)
//						.startAt(Date.from(targetDate.atZone(ZoneId.systemDefault()).toInstant())).build();
//			} else {
//				// Quartz Trigger 생성
//				trigger = TriggerBuilder.newTrigger().forJob(this.globalSlotNumber.getSlotNumberJob())
//						.withIdentity(localDateTimeString+"slotNumberJob", this.group)
//						.startAt(Date.from(targetDate.atZone(ZoneId.systemDefault()).toInstant())).build();
//			}
//			JobDataMap jobDataMap = new JobDataMap();
//			jobDataMap.put("time", targetDate);
//			//System.out.println("보낼시간: "+targetDate);
//			// Quartz JobDetail 생성
//			try {
//				//
//				if (this.globalSlotNumber.getSlotNumberJob() == null) {
//					//
//					JobDetail job = JobBuilder.newJob(SlotNumberCalculateQuartz.class).withIdentity(localDateTimeString, this.group)
//							.storeDurably(true)
//							.setJobData(jobDataMap)
//							.build();
//					this.scheduler.scheduleJob(job, trigger);
//					this.globalSlotNumber.setSlotNumberJob(job);
//				} else {
//					//
//					// 트리거가 이미 존재하는지 확인
//					if (!this.scheduler.checkExists(trigger.getKey())) {
//						// 존재하지 않는 경우 스케줄에 트리거 추가
//						this.scheduler.scheduleJob(trigger);
//						//System.out.println("트리거추가");
//					}
//				}
//			}catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			
//		}else {
//			//
//			String str = "";
//			if(next_ssSSSS < 10) {
//				str = header+":0"+String.format("%06.4f", next_ssSSSS);
//			}else {
//				str = header+":"+String.format("%06.4f", next_ssSSSS);
//			}
//			
//			//System.out.println("str "+str);
//			LocalDateTime targetDate = LocalDateTime.parse(str, formatter);
//			
//			String localDateTimeString = targetDate.toString();
//			//System.out.println("future"+localDateTimeString);
//			
//			Trigger trigger = null;
//			if (this.globalSlotNumber.getSlotNumberJob() == null) {
//				// Quartz Trigger 생성
//				trigger = TriggerBuilder.newTrigger().withIdentity(localDateTimeString, this.group)
//						.startAt(Date.from(targetDate.atZone(ZoneId.systemDefault()).toInstant())).build();
//			} else {
//				// Quartz Trigger 생성
//				trigger = TriggerBuilder.newTrigger().forJob(this.globalSlotNumber.getSlotNumberJob())
//						.withIdentity(localDateTimeString, this.group)
//						.startAt(Date.from(targetDate.atZone(ZoneId.systemDefault()).toInstant())).build();
//			}
//			JobDataMap jobDataMap = new JobDataMap();
//			jobDataMap.put("time", targetDate);
//			//System.out.println("보낼시간: "+targetDate);
//			// Quartz JobDetail 생성
//			try {
//				//
//				if (this.globalSlotNumber.getSlotNumberJob() == null) {
//					//
//					JobDetail job = JobBuilder.newJob(SlotNumberCalculateQuartz.class).withIdentity(localDateTimeString, this.group)
//							.storeDurably(true)
//							.setJobData(jobDataMap)
//							.build();
//					this.scheduler.scheduleJob(job, trigger);
//					this.globalSlotNumber.setSlotNumberJob(job);
//				} else {
//					//
//					// 트리거가 이미 존재하는지 확인
//					if (!this.scheduler.checkExists(trigger.getKey())) {
//						// 존재하지 않는 경우 스케줄에 트리거 추가
//						this.scheduler.scheduleJob(trigger);
//						//System.out.println("트리거추가");
//					}
//				}
//			}catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//		
//	}
//}
