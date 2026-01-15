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
		
		// TCP 서버를 먼저 시작합니다.
		// 선박 생성은 TCP 클라이언트로부터 JSON 메시지를 받아서 처리합니다.
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
				log.info("TCP server started: port {}", tcpPort);
			} else {
				log.error("TCP 서버 시작 실패: 포트 {}", tcpPort);
			}
		} catch (Exception e) {
			log.error("TCP 서버 시작 중 오류 발생: {}", e.getMessage(), e);
		}
		
		log.info("=== application initializer end ===");
		log.info("TCP server: port {}", tcpPort);
	}
}

