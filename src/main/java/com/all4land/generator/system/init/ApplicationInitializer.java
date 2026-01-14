package com.all4land.generator.system.init;

import org.quartz.Scheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.system.netty.send.config.NettyServerTCPConfiguration;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.system.util.BeanUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 애플리케이션 시작 시 자동으로 실행되는 초기화 클래스
 * - 하나의 선박 생성
 * - AIS, ASM 활성화
 * - TCP 서버 시작
 */
@Slf4j
@Component
public class ApplicationInitializer implements CommandLineRunner {

	private final GlobalEntityManager globalEntityManager;
	private final Scheduler scheduler;
	private final QuartzCoreService quartzCoreService;

	public ApplicationInitializer(GlobalEntityManager globalEntityManager, 
			Scheduler scheduler, 
			QuartzCoreService quartzCoreService) {
		this.globalEntityManager = globalEntityManager;
		this.scheduler = scheduler;
		this.quartzCoreService = quartzCoreService;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("[DEBUG] ========== ApplicationInitializer.run() 시작 ==========");
		System.out.println("=== application initializer start ===");
		
		// 1. 하나의 선박 생성 (speed=10, slotTimeout=7)
		System.out.println("[DEBUG] 선박 생성 시작...");
		System.out.println("vessel creation...");
		globalEntityManager.addMmsiEntity10(scheduler, quartzCoreService);
		
		// 생성된 선박 가져오기
		MmsiEntity ship = globalEntityManager.getMmsiEntityLists().get(0);
		System.out.println("[DEBUG] 선박 생성 완료 - MMSI: " + ship.getMmsi());
		System.out.println("vessel creation completed: MMSI=" + ship.getMmsi());
		
		// 2. AIS 활성화 확인 및 강제 활성화
		System.out.println("[DEBUG] AIS 활성화 상태 확인 - MMSI: " + ship.getMmsi() + ", chk: " + ship.isChk());
		if (!ship.isChk()) {
			System.out.println("[DEBUG] AIS가 비활성화되어 있음. 강제로 활성화합니다.");
			ship.setChk(true);
		} else {
			System.out.println("[DEBUG] AIS가 이미 활성화되어 있음");
		}
		System.out.println("AIS activation completed");
		
		// 3. ASM 활성화
		ship.setAsm(true);
		log.info("ASM activation completed");
		
		// 4. TCP 서버 시작 (포트 10110)
		int tcpPort = 10110;
		log.info("TCP 서버 시작 중... (포트: {})", tcpPort);
		
		try {
			// TCP 서버 설정 등록
			BeanUtils.registerBean(tcpPort + "_TcpServer", NettyServerTCPConfiguration.class);
			NettyServerTCPConfiguration tcpServer = (NettyServerTCPConfiguration) BeanUtils.getBean(tcpPort + "_TcpServer");
			
			// UI 의존성 없는 간단한 Bootstrap 설정
			tcpServer.tcpServerBootstrapSimple();
			
			// TCP 서버 시작
			boolean started = tcpServer.startTcp(tcpPort);
			if (started) {
				log.info("TCP 서버 시작 완료: 포트 {}", tcpPort);
				log.info("TCP 클라이언트 연결 대기 중...");
				log.info("테스트 방법: telnet localhost {} 또는 TCP 클라이언트로 연결", tcpPort);
			} else {
				log.error("TCP 서버 시작 실패: 포트 {}", tcpPort);
			}
		} catch (Exception e) {
			log.error("TCP 서버 시작 중 오류 발생: {}", e.getMessage(), e);
		}
		
		log.info("=== 애플리케이션 초기화 완료 ===");
		log.info("선박 정보:");
		log.info("  - MMSI: {}", ship.getMmsi());
		log.info("  - Speed: {}", ship.getSpeed());
		log.info("  - AIS 활성화: {}", ship.isChk());
		log.info("  - ASM 활성화: {}", ship.isAsm());
		log.info("TCP 서버: 포트 {}에서 대기 중", tcpPort);
	}
}

