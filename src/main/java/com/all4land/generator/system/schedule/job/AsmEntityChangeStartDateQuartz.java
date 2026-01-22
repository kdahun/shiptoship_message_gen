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
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.util.RandomGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsmEntityChangeStartDateQuartz implements Job {
	//
	private final GlobalEntityManager globalEntityManager;
	private final TimeMapRangeCompnents timeMapRangeCompnents;
	private final VirtualTimeManager virtualTimeManager;
	private MmsiEntity mmsiEntity;

	public AsmEntityChangeStartDateQuartz(GlobalEntityManager globalEntityManager
			, TimeMapRangeCompnents timeMapRangeCompnents
			, VirtualTimeManager virtualTimeManager) {
		// TODO Auto-generated constructor stub
		this.globalEntityManager = globalEntityManager;
		this.timeMapRangeCompnents = timeMapRangeCompnents;
		this.virtualTimeManager = virtualTimeManager;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		this.mmsiEntity = (MmsiEntity) jobDataMap.get("mmsiEntity");

		// ASM 메시지 생성 여부 확인 (destMMSI 리스트 체크)
		if (!this.mmsiEntity.isAsm()) {
			System.out.println("[DEBUG] ❌ ASM 비활성화 상태 또는 destMMSI 리스트가 비어있음 - MMSI: " + this.mmsiEntity.getMmsi());
			return; // 메시지 생성하지 않음
		}

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
		// destMMSI 리스트가 비어있으면 다음 스케줄 설정하지 않음
		// (메시지 송신 후 이미 asmPeriod=0인 destMMSI가 제거되었으므로)
		if (!this.mmsiEntity.getAsmEntity().hasDestMMSI()) {
			System.out.println("[DEBUG] ✅ destMMSI 리스트가 비어있어 다음 스케줄 설정하지 않음 - MMSI: " + this.mmsiEntity.getMmsi());
			return; // 다음 스케줄 설정하지 않음
		}
		
		// destMMSI 리스트가 비어있지 않으면 다음 스케줄 설정
		// 가상 시간 기준으로 다음 시간 계산
		LocalDateTime currentVirtualTime = virtualTimeManager.getCurrentVirtualTime();
		int randomDelay = RandomGenerator.generateRandomIntFromTo(10, 20);
		LocalDateTime newLocalDateTime = currentVirtualTime.plusSeconds(randomDelay);
		this.mmsiEntity.getAsmEntity().setStartTime(newLocalDateTime, this.mmsiEntity);
		
		System.out.println("[DEBUG] 다음 ASM 메시지 가상 시간: " + newLocalDateTime + 
				" (현재 가상 시간: " + currentVirtualTime + ", delay: " + randomDelay + "초)" +
				", 남은 destMMSI 리스트 크기: " + this.mmsiEntity.getAsmEntity().getDestMMSIList().size());
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




