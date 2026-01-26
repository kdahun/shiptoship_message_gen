package com.all4land.generator.system.schedule.job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.AsmEntity;
import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.entity.TargetCellInfoEntity;
import com.all4land.generator.system.component.TimeMapRangeCompnents;
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.constant.SystemConstMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsmEntityChangeStartDateQuartz implements Job {
	//
	private final GlobalEntityManager globalEntityManager;
	private final TimeMapRangeCompnents timeMapRangeCompnents;
	private final VirtualTimeManager virtualTimeManager;
	private MmsiEntity mmsiEntity;
	private AsmEntity asmEntity;

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
		this.asmEntity = (AsmEntity) jobDataMap.get("asmEntity");
		
		// AsmEntity가 JobDataMap에 없으면 기존 방식으로 가져오기 (호환성)
		if (this.asmEntity == null) {
			this.asmEntity = this.mmsiEntity.getAsmEntity();
			System.out.println("[DEBUG] ⚠️ JobDataMap에서 AsmEntity를 찾을 수 없어 기존 방식으로 가져옴 - MMSI: " + this.mmsiEntity.getMmsi());
		}

		// ASM 메시지 생성 여부 확인 (destMMSI 리스트 체크)
		if (this.asmEntity == null || !this.asmEntity.hasDestMMSI()) {
			System.out.println("[DEBUG] ❌ ASM Entity가 없거나 destMMSI 리스트가 비어있음 - MMSI: " + 
					(this.mmsiEntity != null ? this.mmsiEntity.getMmsi() : "null") + 
					", ServiceId: " + (this.asmEntity != null ? this.asmEntity.getServiceId() : "null"));
			return; // 메시지 생성하지 않음
		}

		// state="0"으로 AsmEntity가 제거되었는지 확인
		// (state="0"일 때 MqttMessageProcessor에서 AsmEntity가 asmEntityMap에서 제거됨)
		String serviceId = this.asmEntity.getServiceId();
		if (serviceId != null) {
			AsmEntity actualAsmEntity = this.mmsiEntity.getAsmEntity(serviceId);
			if (actualAsmEntity == null || actualAsmEntity != this.asmEntity) {
				System.out.println("[DEBUG] ❌ ASM Entity가 MmsiEntity에서 제거됨 (state=0) - MMSI: " + 
						(this.mmsiEntity != null ? this.mmsiEntity.getMmsi() : "null") + 
						", ServiceId: " + serviceId);
				return; // 메시지 생성하지 않음
			}
		}

		int startIndex = this.timeMapRangeCompnents.findSlotNumber(this.asmEntity.getStartTime().format(SystemConstMessage.formatterForStartIndex));
		
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
			CompletableFuture.runAsync(() -> this.globalEntityManager.displayAsm(rule1Value, this.mmsiEntity, this.asmEntity));
		}else {
			if(rule2Value.size() >= 8) {
				//
				CompletableFuture.runAsync(() -> this.globalEntityManager.displayAsm(rule2Value, this.mmsiEntity, this.asmEntity));
			}else {
				if(rule3Value.size() >= 8) {
					//
					CompletableFuture.runAsync(() -> this.globalEntityManager.displayAsm(rule3Value, this.mmsiEntity, this.asmEntity));
				}else {
					
				}
			}
		}
		
		this.addFuture();
		if(this.mmsiEntity.getMmsi() == 336992171) {
			//
			log.info("mmsi : {} , serviceId: {} , {}", this.mmsiEntity.getMmsi(), 
					(this.asmEntity != null ? this.asmEntity.getServiceId() : "null"), LocalDateTime.now());
		}
	}
	
	private void addFuture() {
		//
		// destMMSI 리스트가 비어있으면 다음 스케줄 설정하지 않음
		// (메시지 송신 후 이미 asmPeriod=0인 destMMSI가 제거되었으므로)
		if (!this.asmEntity.hasDestMMSI()) {
			System.out.println("[DEBUG] ✅ destMMSI 리스트가 비어있어 다음 스케줄 설정하지 않음 - MMSI: " + this.mmsiEntity.getMmsi() + 
					", ServiceId: " + this.asmEntity.getServiceId());
			return; // 다음 스케줄 설정하지 않음
		}
		
		// asmPeriod 값 가져오기 (전역 asmPeriod 우선, 없으면 destMMSI 리스트의 최소값 사용)
		String asmPeriodStr = this.asmEntity.getAsmPeriod();
		int periodSeconds = parseAsmPeriod(asmPeriodStr);
		
		// 전역 asmPeriod가 "0"이거나 유효하지 않은 경우, destMMSI 리스트의 최소값 확인
		if (periodSeconds == 0) {
			periodSeconds = getMinAsmPeriodFromDestMMSI();
		}
		
		// asmPeriod가 "0"이면 다음 스케줄 설정하지 않음 (단발 메시지)
		if (periodSeconds == 0) {
			System.out.println("[DEBUG] ✅ ASM Period가 0이어서 다음 스케줄 설정하지 않음 (단발 메시지) - MMSI: " + this.mmsiEntity.getMmsi() + 
					", ServiceId: " + this.asmEntity.getServiceId());
			return;
		}
		
		// destMMSI 리스트가 비어있지 않으면 다음 스케줄 설정
		// 가상 시간 기준으로 다음 시간 계산 (asmPeriod 값 사용)
		LocalDateTime currentVirtualTime = virtualTimeManager.getCurrentVirtualTime();
		LocalDateTime newLocalDateTime = currentVirtualTime.plusSeconds(periodSeconds);
		this.asmEntity.setStartTime(newLocalDateTime, this.mmsiEntity);
		
		System.out.println("[DEBUG] 다음 ASM 메시지 가상 시간: " + newLocalDateTime + 
				" (현재 가상 시간: " + currentVirtualTime + ", asmPeriod: " + periodSeconds + "초)" +
				", MMSI: " + this.mmsiEntity.getMmsi() + ", ServiceId: " + this.asmEntity.getServiceId() +
				", 남은 destMMSI 리스트 크기: " + this.asmEntity.getDestMMSIList().size());
	}
	
	/**
	 * asmPeriod 문자열을 정수로 파싱
	 * @param asmPeriodStr asmPeriod 문자열 ("0" 또는 "4"~"360")
	 * @return 파싱된 초 단위 값 (0 또는 4~360), 파싱 실패 시 0
	 */
	private int parseAsmPeriod(String asmPeriodStr) {
		if (asmPeriodStr == null || asmPeriodStr.isEmpty()) {
			return 0;
		}
		
		try {
			int period = Integer.parseInt(asmPeriodStr);
			// "0"은 단발 메시지
			if (period == 0) {
				return 0;
			}
			// 4~360 범위 검증
			if (period >= 4 && period <= 360) {
				return period;
			} else {
				System.out.println("[DEBUG] ⚠️ ASM Period 범위 초과: " + period + " (4~360 범위여야 함)");
				return 0;
			}
		} catch (NumberFormatException e) {
			System.out.println("[DEBUG] ⚠️ ASM Period 파싱 실패: " + asmPeriodStr + " (숫자여야 함)");
			return 0;
		}
	}
	
	/**
	 * destMMSI 리스트에서 최소 asmPeriod 값 반환
	 * @return 최소 asmPeriod 값 (초 단위), 유효한 값이 없으면 0
	 */
	private int getMinAsmPeriodFromDestMMSI() {
		int minPeriod = Integer.MAX_VALUE;
		boolean foundValidPeriod = false;
		
		for (Long destMMSI : this.asmEntity.getDestMMSIList()) {
			String periodStr = this.asmEntity.getAsmPeriodForDestMMSI(destMMSI);
			if (periodStr != null) {
				int period = parseAsmPeriod(periodStr);
				if (period > 0) {
					minPeriod = Math.min(minPeriod, period);
					foundValidPeriod = true;
				}
			}
		}
		
		return foundValidPeriod ? minPeriod : 0;
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




