package com.all4land.generator.system.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.all4land.generator.system.component.SimulationStateManager;
import com.all4land.generator.system.component.SimulationStateManager.SimulationState;
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.mqtt.MqttClientConfiguration;
import com.all4land.generator.system.netty.dto.VirtualTimeSyncMessage;
import com.all4land.generator.system.util.BeanUtils;
import com.google.gson.Gson;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

/**
 * 가상 시간 동기화 MQTT 메시지 발행 서비스
 * 
 * 시뮬레이션 상태별 동작:
 * - STOPPED: 가상시간 재생, 1분 경계마다 전송
 * - RUNNING: 가상시간 재생, 상태 전환 시 즉시 1회 전송 + 1분 경계마다 전송
 * - PAUSED: 가상시간 정지, 전송 중지
 * 
 * 슬롯 번호 없이 분 단위 비교만 사용하며, 배속이 높을수록 폴링 주기를 짧게 해서
 * 발행 시점이 프레임 시작에 가깝게 맞춰진다.
 */
@Slf4j
@Service
public class VirtualTimeSyncMqttService {

	private final VirtualTimeManager virtualTimeManager;
	private final SimulationStateManager simulationStateManager;
	private final Gson gson = new Gson();

	/** 이전 폴링 시점의 가상 시간(분 단위 절사). null이면 아직 한 번도 안 본 상태 */
	private LocalDateTime lastMinuteBoundary = null;
	
	/** 이전 시뮬레이션 상태 (상태 전환 감지용) */
	private SimulationState lastState = SimulationState.STOPPED;

	/** 배속에 따라 다음 폴링까지 대기할 실제 시간(ms). 1배=100ms, 8배=12.5ms */
	private static final long BASE_POLL_MS = 100L;
	private static final long MIN_POLL_MS = 12L;

	/** 1분 = 2250슬롯(0~2249), 슬롯당 초 */
	private static final double SECONDS_PER_SLOT = 60.0 / 2250;
	private static final int MAX_SLOT = 2249;

	private final DateTimeFormatter topicFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSS");

	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
		Thread t = new Thread(r, "virtual-time-sync");
		t.setDaemon(false);
		return t;
	});

	public VirtualTimeSyncMqttService(
			VirtualTimeManager virtualTimeManager,
			SimulationStateManager simulationStateManager) {
		this.virtualTimeManager = virtualTimeManager;
		this.simulationStateManager = simulationStateManager;
	}

	@PostConstruct
	public void startScheduling() {
		scheduler.schedule(this::tick, 0, TimeUnit.MILLISECONDS);
	}

	@PreDestroy
	public void stopScheduling() {
		scheduler.shutdown();
		try {
			if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
				scheduler.shutdownNow();
			}
		} catch (InterruptedException e) {
			scheduler.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 배속을 반영한 폴링 주기(ms). 8배속이면 약 12ms로 짧게 해서 프레임 경계를 빨리 감지.
	 */
	private long getPollDelayMs() {
		double speed = virtualTimeManager.getSpeedMultiplier();
		long delay = (long) (BASE_POLL_MS / speed);
		return Math.max(MIN_POLL_MS, delay);
	}

	private void tick() {
		try {
			checkAndPublishTimeSync();
		} catch (Exception e) {
			log.error("❌ Virtual time-sync 체크 중 오류 발생: {}", e.getMessage(), e);
		} finally {
			scheduler.schedule(this::tick, getPollDelayMs(), TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * 가상 시간이 새 분으로 넘어갔는지 확인하고, 시뮬레이션 상태에 따라 MQTT 발행.
	 * - PAUSED: 전송 중지
	 * - RUNNING 전환 시: 즉시 1회 전송
	 * - STOPPED/RUNNING: 1분 경계마다 전송
	 */
	public void checkAndPublishTimeSync() {
		LocalDateTime virtualTime = virtualTimeManager.getCurrentVirtualTime();
		LocalDateTime currentMinuteBoundary = virtualTime.truncatedTo(ChronoUnit.MINUTES);
		
		// 현재 시뮬레이션 상태 조회
		SimulationState currentState = simulationStateManager.getCurrentState();
		
		// PAUSED 상태: 전송 중지
		if (currentState == SimulationState.PAUSED) {
			lastState = currentState;
			return;
		}
		
		// RUNNING 전환 감지 시 즉시 전송
		if (currentState == SimulationState.RUNNING && lastState != SimulationState.RUNNING) {
			int slotAtSend = getSlotNumber(virtualTime);
			publishTimeSyncMessage(virtualTime);
			log.info("✅ Virtual time-sync MQTT 발행 - RUNNING 전환 ({}→RUNNING) - 현재 가상시간: {}, 슬롯: {}",
					lastState, virtualTime, slotAtSend);
		}
		
		// 이전 상태 업데이트
		lastState = currentState;

		if (lastMinuteBoundary == null) {
			lastMinuteBoundary = currentMinuteBoundary;
			return;
		}

		// 분 경계 감지 (STOPPED/RUNNING 상태에서 동작)
		if (currentMinuteBoundary.isAfter(lastMinuteBoundary)) {
			int slotAtSend = getSlotNumber(virtualTime);
			publishTimeSyncMessage(virtualTime);
			log.info("✅ Virtual time-sync MQTT 발행 - 분 경계 감지 - frameStart: {} (현재 가상시간: {}, 슬롯: {})",
					currentMinuteBoundary, virtualTime, slotAtSend);
		}

		lastMinuteBoundary = currentMinuteBoundary;
	}

	/**
	 * 가상 시간이 속한 슬롯 번호(0~2249). 로그 출력용.
	 */
	private int getSlotNumber(LocalDateTime virtualTime) {
		double secondsInMinute = virtualTime.getSecond() + virtualTime.getNano() / 1_000_000_000.0;
		int slot = (int) (secondsInMinute / SECONDS_PER_SLOT);
		return Math.min(slot, MAX_SLOT);
	}

	/**
	 * MQTT 메시지 발행
	 * 토픽: mg/ms/time-sync/{OS시간}
	 * 페이로드: [{"virtualTime":"현재 가상시간 yyyyMMddHHmmss.SSSS"}] (로그에 출력하는 값과 동일)
	 * 연결·발행은 ApplicationInitializer에서 connect() 호출하는 "mqttClient" 빈을 사용.
	 */
	private void publishTimeSyncMessage(LocalDateTime virtualTime) {
		MqttClientConfiguration client = (MqttClientConfiguration) BeanUtils.getBean("mqttClient");
		if (client == null || !client.isConnected()) {
			log.warn("⚠️ MQTT 클라이언트가 연결되지 않았습니다. Virtual time-sync 메시지 발행 스킵");
			return;
		}

		try {
			String osTimestamp = LocalDateTime.now().format(topicFormatter);
			String topic = "mg/ms/time-sync/" + osTimestamp;

			VirtualTimeSyncMessage message = new VirtualTimeSyncMessage();
			message.setVirtualTime(virtualTime.format(topicFormatter));

			List<VirtualTimeSyncMessage> messageList = Arrays.asList(message);
			String payload = gson.toJson(messageList);

			client.publish(topic, payload, 1, false);

			log.debug("MQTT 발행 완료 - Topic: {}, Payload: {}", topic, payload);

		} catch (Exception e) {
			log.error("❌ Virtual time-sync MQTT 발행 중 오류: {}", e.getMessage(), e);
		}
	}
}
