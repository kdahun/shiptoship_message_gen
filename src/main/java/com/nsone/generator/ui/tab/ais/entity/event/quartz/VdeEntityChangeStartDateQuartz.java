package com.nsone.generator.ui.tab.ais.entity.event.quartz;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nsone.generator.system.constant.SystemConstMessage;
import com.nsone.generator.ui.tab.ais.entity.GlobalEntityManager;
import com.nsone.generator.ui.tab.ais.entity.MmsiEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VdeEntityChangeStartDateQuartz implements Job {
	//
	private final GlobalEntityManager globalEntityManager;
	private MmsiEntity mmsiEntity;

	public VdeEntityChangeStartDateQuartz(GlobalEntityManager globalEntityManager) {
		// TODO Auto-generated constructor stub
		this.globalEntityManager = globalEntityManager;
	}
	
//	@Async
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		this.mmsiEntity = (MmsiEntity) jobDataMap.get("mmsiEntity");

		log.info("VDE quartz.....");
//		CompletableFuture<List<TargetCellInfoEntity>> rule1 = CompletableFuture.supplyAsync(() -> {
//		    return this.findAsmRule1(); });
//		CompletableFuture<List<TargetCellInfoEntity>> rule2 = CompletableFuture.supplyAsync(() -> {
//		    return this.findAsmRule2(); });
//		CompletableFuture<List<TargetCellInfoEntity>> rule3 = CompletableFuture.supplyAsync(() -> {
//		    return this.findAsmRule3(); });
//		
//		List<TargetCellInfoEntity> rule1Value = rule1.join();
//		List<TargetCellInfoEntity> rule2Value = rule2.join();
//		List<TargetCellInfoEntity> rule3Value = rule3.join();
//		
//		CompletableFuture<Void> allOfRule = CompletableFuture.allOf(rule1, rule2, rule3);
//
//		allOfRule.join(); // 모든 작업이 완료될 때까지 대기
//		
//		if(rule1Value.size() >= 8) {
//			//
////			log.info("Rule 1 find");
//			this.globalEntityManager.displayAsm(rule1Value, this.mmsiEntity);
//		}else if(rule2Value.size() >= 8) {
//			//
//			log.info("Rule 2 find");
//			this.globalEntityManager.displayAsm(rule2Value, this.mmsiEntity);
//		}else if(rule3Value.size() >= 8) {
//			//
//			log.info("Rule 3 find");
//			this.globalEntityManager.displayAsm(rule3Value, this.mmsiEntity);
//		}
//		
//		
		this.globalEntityManager.findVde(getStartIndex(), mmsiEntity);
		this.addFuture();
//		if(this.mmsiEntity.getMmsi() == 336992171) {
//			//
//			log.info("mmsi : {} , {}", this.mmsiEntity.getMmsi(), LocalDateTime.now());
////			System.out.println("mmsi : "+this.mmsiEntity.getMmsi());
//		}
	}
	
	private void addFuture() {
		//
		LocalDateTime newLocalDateTime = this.mmsiEntity.getVdeEntity().getStartTime().plusSeconds(60);
		this.mmsiEntity.getVdeEntity().setStartTime(newLocalDateTime, this.mmsiEntity);

	}
	
	
	
	private int getStartIndex() {
		//
		String ssSSSS = this.mmsiEntity.getVdeEntity().getStartTime().format(SystemConstMessage.formatterForStartIndex);

		double divisor = 0.0266666666666667;
		double currentSecond = Double.valueOf(ssSSSS);

		int tmp = (int) (currentSecond / divisor);

		if (tmp <= 5) {
			tmp = 0;
		} else {
			tmp = tmp - 5;
		}

		return tmp;
	}
	
}

