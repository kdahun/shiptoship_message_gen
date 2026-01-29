package com.all4land.generator.system.component;

import java.time.LocalDateTime;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.AsmEntity;
import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.system.schedule.QuartzCoreService;

import lombok.extern.slf4j.Slf4j;

/**
 * 시뮬레이션 상태 관리 컴포넌트
 * RUN/PAUSE/STOP 상태를 관리하고 Quartz Scheduler를 제어합니다.
 */
@Slf4j
@Component
public class SimulationStateManager {
	
	public enum SimulationState {
		STOPPED,   // state=4, 초기 상태
		RUNNING,   // state=1
		PAUSED     // state=2
	}
	
	private volatile SimulationState currentState = SimulationState.STOPPED;
	
	private final VirtualTimeManager virtualTimeManager;
	private final Scheduler scheduler;
	private final GlobalEntityManager globalEntityManager;
	private final QuartzCoreService quartzCoreService;
	
	public SimulationStateManager(VirtualTimeManager virtualTimeManager,
	                               Scheduler scheduler,
	                               GlobalEntityManager globalEntityManager,
	                               QuartzCoreService quartzCoreService) {
		this.virtualTimeManager = virtualTimeManager;
		this.scheduler = scheduler;
		this.globalEntityManager = globalEntityManager;
		this.quartzCoreService = quartzCoreService;
	}
	
	public boolean isPaused() {
		return currentState == SimulationState.PAUSED;
	}
	
	public boolean isRunning() {
		return currentState == SimulationState.RUNNING;
	}
	
	public SimulationState getCurrentState() {
		return currentState;
	}
	
	/**
	 * RUN/RESUME 처리
	 * - STOPPED → RUNNING: 새로 시작
	 * - PAUSED → RUNNING: 가상시간 복원 + 트리거 재등록 + Scheduler 재개
	 */
	public synchronized void run() {
		try {
			if (currentState == SimulationState.PAUSED) {
				// RESUME 절차:
				
				// 1️⃣ 가상시간 복원 (PAUSE 시점부터 다시 흐르게)
				virtualTimeManager.resumeTime();
				
				// 2️⃣ 모든 활성 엔티티의 트리거 재등록
				// (기존 트리거는 과거 startAt이므로 재계산 필요)
				rescheduleAllJobs();
				
				// 3️⃣ Scheduler 재개
				scheduler.start();
				
				log.info("시뮬레이션 재개 완료 - Scheduler started, 트리거 재등록 완료");
			} else if (currentState == SimulationState.STOPPED) {
				// 새로 시작
				if (scheduler.isInStandbyMode()) {
					scheduler.start();
				}
				log.info("시뮬레이션 시작 - Scheduler started");
			}
			
			currentState = SimulationState.RUNNING;
			
		} catch (Exception e) {
			log.error("시뮬레이션 시작/재개 중 오류 발생", e);
		}
	}
	
	/**
	 * RESUME 시 모든 활성 Job의 트리거 재등록
	 * 기존 startTime(가상시간)을 현재 실제시간 기준으로 재계산하여 트리거 등록
	 */
	private void rescheduleAllJobs() {
		try {
			int rescheduledCount = 0;
			
			for (MmsiEntity entity : globalEntityManager.getMmsiEntityLists()) {
				if (entity.isChk() && entity.getStartTime() != null) {
					// 기존 startTime (가상시간) - 그대로 유지
					LocalDateTime virtualStartTime = entity.getStartTime();
					
					// 기존 Job/트리거 제거 (있다면)
					if (entity.getJob() != null) {
						try {
							quartzCoreService.removeStartTimeTrigger(entity);
						} catch (Exception e) {
							log.warn("MMSI {} 기존 트리거 제거 실패: {}", entity.getMmsi(), e.getMessage());
						}
					}
					
					// setStartTime 호출 → 이벤트 발행 → 새 트리거 등록
					// (내부적으로 convertVirtualToRealTime 사용하여 현재 기준으로 재계산)
					entity.setStartTime(virtualStartTime);
					
					rescheduledCount++;
					
					log.debug("MMSI {} 트리거 재등록 - 가상시간: {}", 
						entity.getMmsi(), virtualStartTime);
					
					// ASM 엔티티들도 재스케줄
					for (AsmEntity asmEntity : entity.getAllAsmEntities().values()) {
						if (asmEntity != null && asmEntity.hasTestMMSI() && 
							asmEntity.getStartTime() != null) {
							LocalDateTime asmVirtualStartTime = asmEntity.getStartTime();
							asmEntity.setStartTime(asmVirtualStartTime, entity);
						}
					}
					
					// VDE 엔티티도 재스케줄
					if (entity.getVdeEntity() != null && 
						entity.getVdeEntity().getStartTime() != null) {
						LocalDateTime vdeVirtualStartTime = entity.getVdeEntity().getStartTime();
						entity.getVdeEntity().setStartTime(vdeVirtualStartTime, entity);
					}
				}
			}
			
			log.info("모든 활성 Job 트리거 재등록 완료 - 재등록 개수: {}", rescheduledCount);
			
		} catch (Exception e) {
			log.error("Job 트리거 재등록 중 오류 발생", e);
		}
	}
	
	/**
	 * PAUSE 처리
	 * Scheduler를 standby 모드로 전환 + 가상시간 정지
	 */
	public synchronized void pause() {
		if (currentState != SimulationState.RUNNING) {
			log.warn("RUNNING 상태가 아니므로 PAUSE 무시. 현재 상태: {}", currentState);
			return;
		}
		
		try {
			// 가상시간 정지 (먼저 호출 - getCurrentVirtualTime()이 PAUSE 전 시간을 가져와야 함)
			virtualTimeManager.pauseTime();
			
			// Scheduler standby 모드로 전환
			scheduler.standby();
			
			currentState = SimulationState.PAUSED;
			
			log.info("시뮬레이션 일시정지 - Scheduler standby, 가상시간: {}", 
				virtualTimeManager.getPausedVirtualTime());
			
		} catch (SchedulerException e) {
			log.error("시뮬레이션 일시정지 중 오류 발생", e);
		}
	}
	
	/**
	 * STOP 처리 - 완전 초기화
	 */
	public synchronized void stop() {
		try {
			scheduler.clear(); // 모든 Quartz Job 삭제
			globalEntityManager.clearAllState(); // 엔티티 초기화
			virtualTimeManager.reset(); // 가상시간 초기화
			
			currentState = SimulationState.STOPPED;
			
			log.info("시뮬레이션 완전 중단 및 초기화 완료");
			
		} catch (SchedulerException e) {
			log.error("STOP 처리 중 오류 발생", e);
		}
	}
}
