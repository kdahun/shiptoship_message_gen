package com.all4land.generator.system.component;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.system.constant.TimeMode;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 시뮬레이션 배속/저속 기능을 위한 가상 시간 관리 컴포넌트
 * 
 * 배속 기능:
 * - speedMultiplier = 1.0: 실시간 (1배속)
 * - speedMultiplier = 2.0: 2배속
 * - speedMultiplier = 4.0: 4배속
 * - speedMultiplier = 8.0: 8배속
 * 
 * 동작 원리:
 * 1. 프로그램 시작 시점의 실제 시간을 기준점(baseRealTime)으로 설정
 * 2. 이후 모든 가상 시간은: 
 *    virtualTime = baseVirtualTime + (실제 경과 시간 * speedMultiplier)
 * 3. 모든 슬롯 계산과 스케줄링은 이 가상 시간을 사용
 */
@Slf4j
@Component
public class VirtualTimeManager {
	
	@Getter
	private TimeMode timeMode = TimeMode.VIRTUAL;
	
	@Getter
	private double speedMultiplier = 1.0; // 기본값: 실시간
	
	private LocalDateTime baseRealTime; // 기준 실제 시간
	private LocalDateTime baseVirtualTime; // 기준 가상 시간
	private long lastUpdateNanos; // 마지막 업데이트 시간 (나노초)
	
	// PAUSE/RESUME 기능을 위한 필드
	private LocalDateTime pausedVirtualTime; // PAUSE 시점의 가상시간
	private LocalDateTime pausedRealTime; // PAUSE 시점의 실제시간
	private boolean isPaused = false; // PAUSE 상태 여부
	
	// 허용되는 배속 값
	private static final double[] ALLOWED_SPEEDS = {1.0, 2.0, 4.0, 8.0};
	
	public VirtualTimeManager() {
		// 
	}
	
	@PostConstruct
	public void init() {
		LocalDateTime now = LocalDateTime.now();
		this.baseRealTime = now;
		this.baseVirtualTime = now;
		this.lastUpdateNanos = System.nanoTime();
		log.info("VirtualTimeManager 초기화 완료 - 기준 시간: {}, 배속: {}배, 모드: {}", 
				now, speedMultiplier, timeMode);
	}
	
	/**
	 * 현재 가상 시간을 반환
	 * 배속이 적용된 가상 시뮬레이션 시간
	 * PAUSE 중이면 pausedVirtualTime을 계속 반환 (고정된 시간)
	 */
	public LocalDateTime getCurrentVirtualTime() {
		// PAUSE 중이면 고정된 시간 반환
		if (isPaused && pausedVirtualTime != null) {
			return pausedVirtualTime;
		}
		
		if (timeMode == TimeMode.SYSTEM) {
			return LocalDateTime.now();
		}
		
		long currentNanos = System.nanoTime();
		long elapsedNanos = currentNanos - lastUpdateNanos;
		long elapsedNanosWithSpeed = (long)(elapsedNanos * speedMultiplier);
		
		return baseVirtualTime.plusNanos(elapsedNanosWithSpeed);
	}
	
	/**
	 * 배속 변경
	 * 배속은 1, 2, 4, 8배만 허용됩니다.
	 * 
	 * @param newSpeed 배속 값 (1, 2, 4, 8만 허용)
	 * @return 성공 여부
	 */
	public boolean setSpeedMultiplier(double newSpeed) {
		if (timeMode == TimeMode.SYSTEM) {
			log.warn("SYSTEM 시간 모드에서는 배속을 변경할 수 없습니다.");
			return false;
		}
		
		// 허용되는 배속 값인지 확인
		boolean isValidSpeed = false;
		for (double allowedSpeed : ALLOWED_SPEEDS) {
			if (Math.abs(newSpeed - allowedSpeed) < 0.001) {
				isValidSpeed = true;
				newSpeed = allowedSpeed; // 정확한 값으로 설정
				break;
			}
		}
		
		if (!isValidSpeed) {
			log.warn("배속은 1, 2, 4, 8배만 허용됩니다. 요청한 값: {}", newSpeed);
			return false;
		}
		
		// 현재 가상 시간 저장
		LocalDateTime currentVirtualTime = getCurrentVirtualTime();
		
		// 기준점 재설정
		this.baseVirtualTime = currentVirtualTime;
		this.baseRealTime = LocalDateTime.now();
		this.lastUpdateNanos = System.nanoTime();
		this.speedMultiplier = newSpeed;
		
		log.info("배속 변경: {}배 - 새 기준 가상시간: {}", newSpeed, currentVirtualTime);
		return true;
	}
	
	/**
	 * 배속을 문자열로 설정 (MQTT 메시지 처리용)
	 * @param speedStr 배속 값 문자열 ("1", "2", "4", "8")
	 * @return 성공 여부
	 */
	public boolean setSpeedMultiplier(String speedStr) {
		try {
			int speedInt = Integer.parseInt(speedStr.trim());
			return setSpeedMultiplier((double) speedInt);
		} catch (NumberFormatException e) {
			log.warn("유효하지 않은 배속 값: {}", speedStr);
			return false;
		}
	}
	
	/**
	 * 지정된 실제 시간 이후의 가상 시간을 계산
	 * Quartz 스케줄러 트리거 시간 계산용
	 */
	public LocalDateTime calculateVirtualTimeAfter(LocalDateTime realTime, long durationSeconds) {
		if (timeMode == TimeMode.SYSTEM) {
			return realTime.plusSeconds(durationSeconds);
		}
		return realTime.plusSeconds((long)(durationSeconds * speedMultiplier));
	}
	
	/**
	 * 실제 시간 기준으로 가상 시간 범위 계산
	 */
	public LocalDateTime[] calculateVirtualTimeRange(LocalDateTime realTime, 
			double secondsBefore, double secondsAfter) {
		if (timeMode == TimeMode.SYSTEM) {
			LocalDateTime virtualStart = realTime.minusNanos((long)(secondsBefore * 1_000_000_000));
			LocalDateTime virtualEnd = realTime.plusNanos((long)(secondsAfter * 1_000_000_000));
			return new LocalDateTime[] {virtualStart, virtualEnd};
		}
		
		LocalDateTime virtualStart = realTime.minusNanos(
				(long)(secondsBefore * 1_000_000_000 * speedMultiplier));
		LocalDateTime virtualEnd = realTime.plusNanos(
				(long)(secondsAfter * 1_000_000_000 * speedMultiplier));
		return new LocalDateTime[] {virtualStart, virtualEnd};
	}
	
	/**
	 * 현재 가상 시간의 초 단위 문자열 반환 (슬롯 계산용)
	 */
	public String getCurrentVirtualTimeForSlot() {
		return getCurrentVirtualTime().format(SystemConstMessage.formatterForStartIndex);
	}
	
	/**
	 * 가상 시간을 실제 시간으로 변환
	 * Quartz 스케줄러 트리거 시간 계산용
	 */
	public LocalDateTime convertVirtualToRealTime(LocalDateTime virtualTime) {
		if (timeMode == TimeMode.SYSTEM) {
			return virtualTime;
		}
		
		LocalDateTime currentVirtualTime = getCurrentVirtualTime();
		long virtualDiffFromCurrentNanos = 
				java.time.Duration.between(virtualTime, currentVirtualTime).toNanos();
		
		long realDiffNanos = (long)(virtualDiffFromCurrentNanos / speedMultiplier);
		LocalDateTime now = LocalDateTime.now();
		
		return now.minusNanos(realDiffNanos);
	}
	
	/**
	 * 시간 모드 변경
	 */
	public void setTimeMode(TimeMode timeMode) {
		if (timeMode == null) {
			log.warn("null 시간 모드는 허용되지 않습니다.");
			return;
		}
		
		if (this.timeMode == timeMode) {
			log.info("이미 {} 시간 모드입니다.", timeMode);
			return;
		}
		
		this.timeMode = timeMode;
		LocalDateTime now = LocalDateTime.now();
		this.baseRealTime = now;
		this.baseVirtualTime = now;
		this.lastUpdateNanos = System.nanoTime();
		
		if (timeMode == TimeMode.SYSTEM) {
			this.speedMultiplier = 1.0;
			log.info("시간 모드가 SYSTEM(실제 시간)으로 변경되었습니다.");
		} else {
			log.info("시간 모드가 VIRTUAL(가상 시간)로 변경되었습니다. 현재 배속: {}배", speedMultiplier);
		}
	}
	
	/**
	 * PAUSE 시 가상시간 정지
	 * 현재 가상시간을 저장하고, getCurrentVirtualTime()이 고정된 시간을 반환하도록 함
	 */
	public void pauseTime() {
		pausedVirtualTime = getCurrentVirtualTime();
		pausedRealTime = LocalDateTime.now();
		isPaused = true;
		log.info("가상시간 정지 - pausedVirtualTime: {}, pausedRealTime: {}", 
				pausedVirtualTime, pausedRealTime);
	}
	
	/**
	 * RESUME 시 가상시간 재개
	 * 가상시간 기준점을 pausedVirtualTime으로 재설정하여 PAUSE 시점부터 다시 흐르게 함
	 */
	public void resumeTime() {
		if (pausedVirtualTime != null) {
			// 가상시간 기준점을 pausedVirtualTime으로 재설정
			this.baseVirtualTime = pausedVirtualTime;
			this.baseRealTime = LocalDateTime.now();
			this.lastUpdateNanos = System.nanoTime();
			isPaused = false;
			log.info("가상시간 재개 - baseVirtualTime: {}, baseRealTime: {}", 
					pausedVirtualTime, this.baseRealTime);
		} else {
			log.warn("pausedVirtualTime이 null입니다. resumeTime() 실행 불가.");
		}
	}
	
	/**
	 * PAUSE된 가상시간 반환 (디버깅용)
	 */
	public LocalDateTime getPausedVirtualTime() {
		return pausedVirtualTime;
	}
	
	/**
	 * PAUSE 상태 여부 반환
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * STOP 시 완전 초기화
	 * 모든 PAUSE 관련 상태를 초기화하고, 배속을 1배속으로 리셋하고, 시간 기준점을 재설정
	 */
	public void reset() {
		isPaused = false;
		pausedVirtualTime = null;
		pausedRealTime = null;
		speedMultiplier = 1.0; // 배속을 1배속으로 초기화
		init(); // @PostConstruct 로직 재실행
	}
	
	/**
	 * 디버그용: 현재 시간 상태 출력
	 */
	public void printCurrentStatus() {
		log.info("========== VirtualTimeManager 상태 ==========");
		log.info("시간 모드: {}", timeMode);
		log.info("배속: {}배", speedMultiplier);
		log.info("PAUSE 상태: {}", isPaused);
		if (isPaused) {
			log.info("PAUSE된 가상시간: {}", pausedVirtualTime);
			log.info("PAUSE된 실제시간: {}", pausedRealTime);
		}
		log.info("기준 실제 시간: {}", baseRealTime);
		log.info("기준 가상 시간: {}", baseVirtualTime);
		log.info("현재 실제 시간: {}", LocalDateTime.now());
		log.info("현재 가상 시간: {}", getCurrentVirtualTime());
		log.info("============================================");
	}
}

