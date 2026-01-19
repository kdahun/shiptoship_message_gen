package com.all4land.generator.system.schedule.job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.entity.TargetCellInfoEntity;
import com.all4land.generator.system.component.TimeMapRangeCompnents;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.util.RandomGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsmEntityChangeStartDateQuartz implements Job {
	//
	private final GlobalEntityManager globalEntityManager;
	private final TimeMapRangeCompnents timeMapRangeCompnents;
	private MmsiEntity mmsiEntity;

	public AsmEntityChangeStartDateQuartz(GlobalEntityManager globalEntityManager
			, TimeMapRangeCompnents timeMapRangeCompnents) {
		// TODO Auto-generated constructor stub
		this.globalEntityManager = globalEntityManager;
		this.timeMapRangeCompnents = timeMapRangeCompnents;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		this.mmsiEntity = (MmsiEntity) jobDataMap.get("mmsiEntity");

		int startIndex = this.timeMapRangeCompnents.findSlotNumber(this.mmsiEntity.getAsmEntity().getStartTime().format(SystemConstMessage.formatterForStartIndex));
		
		CompletableFuture<List<TargetCellInfoEntity>> rule1 = CompletableFuture.supplyAsync(() -> {
		    return this.findAsmRule1(startIndex); });
		CompletableFuture<List<TargetCellInfoEntity>> rule2 = CompletableFuture.supplyAsync(() -> {
		    return this.findAsmRule2(startIndex); });
		CompletableFuture<List<TargetCellInfoEntity>> rule3 = CompletableFuture.supplyAsync(() -> {
		    return this.findAsmRule3(startIndex); });
		
		List<TargetCellInfoEntity> rule1Value = rule1.join();
		List<TargetCellInfoEntity> rule2Value = rule2.join();
		List<TargetCellInfoEntity> rule3Value = rule3.join();
		
		CompletableFuture<Void> allOfRule = CompletableFuture.allOf(rule1, rule2, rule3);

		allOfRule.join(); // 모든 작업이 완료될 때까지 대기
		
		// 타겟 cell이 8개 이상인 경우에만 displayAsm 호출
		if(rule1Value.size() >= 8) {
			CompletableFuture.runAsync(() -> this.globalEntityManager.displayAsm(rule1Value, this.mmsiEntity));
		}else {
			if(rule2Value.size() >= 8) {
				//
				CompletableFuture.runAsync(() -> this.globalEntityManager.displayAsm(rule2Value, this.mmsiEntity));
			}else {
				if(rule3Value.size() >= 8) {
					//
					CompletableFuture.runAsync(() -> this.globalEntityManager.displayAsm(rule3Value, this.mmsiEntity));
				}else {
					
				}
			}
		}
		
		this.addFuture();
		if(this.mmsiEntity.getMmsi() == 336992171) {
			//
			log.info("mmsi : {} , {}", this.mmsiEntity.getMmsi(), LocalDateTime.now());
		}
	}
	
	private void addFuture() {
		//
		int randomDelay = RandomGenerator.generateRandomIntFromTo(10, 20);
		LocalDateTime newLocalDateTime = this.mmsiEntity.getAsmEntity().getStartTime().plusSeconds(randomDelay);
		this.mmsiEntity.getAsmEntity().setStartTime(newLocalDateTime, this.mmsiEntity);
	}
	
	private List<TargetCellInfoEntity> findAsmRule1(int startIndex) {
		//
		return this.globalEntityManager.findAsmRule1(startIndex, mmsiEntity);
	}
	
	private List<TargetCellInfoEntity> findAsmRule2(int startIndex) {
		//
		return this.globalEntityManager.findAsmRule2(startIndex, mmsiEntity);
	}
	
	private List<TargetCellInfoEntity> findAsmRule3(int startIndex) {
		//
		return this.globalEntityManager.findAsmRule3(startIndex, mmsiEntity);
	}
}




