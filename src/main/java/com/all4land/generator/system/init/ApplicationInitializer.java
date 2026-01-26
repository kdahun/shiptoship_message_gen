package com.all4land.generator.system.init;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.SlotStateManager;
import com.all4land.generator.system.component.TimeMapRangeCompnents;
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.mqtt.MqttClientConfiguration;
import com.all4land.generator.system.mqtt.MqttMessageProcessor;
import com.all4land.generator.system.netty.send.config.NettyServerTCPConfiguration;
import com.all4land.generator.system.queue.TsqMessageQueue;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.system.component.ApplicationContextProvider;
import com.all4land.generator.system.util.BeanUtils;
import com.all4land.generator.ui.tab.ais.model.TcpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpServerTableModel;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 애플리케이션 시작 시 자동으로 실행되는 초기화 클래스
 * - TCP 서버 시작
 * - MQTT 클라이언트 연결 및 구독
 */
@Slf4j
@Component
public class ApplicationInitializer implements CommandLineRunner {

	private final GlobalEntityManager globalEntityManager;
	private final Scheduler scheduler;
	private final QuartzCoreService quartzCoreService;
	private final VirtualTimeManager virtualTimeManager;
	private final ApplicationEventPublisher eventPublisher;
	private final TcpServerTableModel tcpServerTableModel;
	private final UdpServerTableModel udpServerTableModel;
	private final TimeMapRangeCompnents timeMapRangeCompnents;
	private final SlotStateManager slotStateManager;
	private final TsqMessageQueue tsqMessageQueue;
	
	// MQTT 설정 (application.properties에서 읽어옴)
	@Value("${mqtt.broker.url:tcp://localhost:1883}")
	private String mqttBrokerUrl;
	
	@Value("${mqtt.client.id:AISGenerator}")
	private String mqttClientIdPrefix;
	
	@Value("${mqtt.topic.pattern:+/+/traffic-ships/create/+}")
	private String mqttTopicPattern;

	public ApplicationInitializer(GlobalEntityManager globalEntityManager, 
			Scheduler scheduler, 
			QuartzCoreService quartzCoreService,
			VirtualTimeManager virtualTimeManager,
			ApplicationEventPublisher eventPublisher,
			TcpServerTableModel tcpServerTableModel,
			UdpServerTableModel udpServerTableModel,
			TimeMapRangeCompnents timeMapRangeCompnents,
			SlotStateManager slotStateManager,
			TsqMessageQueue tsqMessageQueue) {
		this.globalEntityManager = globalEntityManager;
		this.scheduler = scheduler;
		this.quartzCoreService = quartzCoreService;
		this.virtualTimeManager = virtualTimeManager;
		this.eventPublisher = eventPublisher;
		this.tcpServerTableModel = tcpServerTableModel;
		this.udpServerTableModel = udpServerTableModel;
		this.timeMapRangeCompnents = timeMapRangeCompnents;
		this.slotStateManager = slotStateManager;
		this.tsqMessageQueue = tsqMessageQueue;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("[DEBUG] ========== ApplicationInitializer.run() 시작 ==========");
		System.out.println("=== application initializer start ===");
		
		// 1. TCP 서버 시작
		startTcpServer();
		
		// 2. MQTT 클라이언트 연결 및 구독
		startMqttClient();
		
		log.info("=== application initializer end ===");
	}
	
	/**
	 * TCP 서버를 시작합니다.
	 */
	private void startTcpServer() {
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
	}
	
	/**
	 * MQTT 클라이언트를 연결하고 구독을 시작합니다.
	 * 
	 * 토픽 형식: {송신 식별자}/{수신 식별자}/{카테고리}/{액션}/{timestamp}
	 * 예시: 
	 *   - mt/mg/traffic-ships/create/20260116112354 (선박 생성)
	 *   - mt/mg/traffic-ships/ais-state/20260116112354 (AIS 상태 제어)
	 * 
	 * 와일드카드 패턴:
	 *   - mt/mg/traffic-ships/create/+ (선박 생성 토픽)
	 *   - mt/mg/traffic-ships/ais-state/+ (AIS 상태 제어 토픽)
	 *   - mt/mg/+/+/+ (mt/mg로 시작하는 모든 토픽)
	 */
	private void startMqttClient() {
		// MQTT 설정 (application.properties에서 읽어옴, 시스템 프로퍼티로도 오버라이드 가능)
		// 시스템 프로퍼티가 있으면 우선 사용, 없으면 application.properties 값 사용
		String brokerUrl = System.getProperty("mqtt.broker.url", mqttBrokerUrl);
		String clientId = System.getProperty("mqtt.client.id", mqttClientIdPrefix + "_" + System.currentTimeMillis());
		String topicPattern = System.getProperty("mqtt.topic.pattern", mqttTopicPattern);
		
		String[] mqttTopics = topicPattern.split(",");
		
		// 각 토픽의 QoS 레벨 설정 (기본값: 1)
		int[] mqttQos = new int[mqttTopics.length];
		for (int i = 0; i < mqttTopics.length; i++) {
			String qosKey = "mqtt.qos." + i;
			mqttQos[i] = Integer.parseInt(System.getProperty(qosKey, "1"));
		}
		
		
		try {
			// MQTT 클라이언트 설정 등록
			BeanUtils.registerBean("mqttClient", MqttClientConfiguration.class);
			MqttClientConfiguration mqttClient = (MqttClientConfiguration) BeanUtils.getBean("mqttClient");
			
			// MQTT 클라이언트 초기화
			mqttClient.initialize(brokerUrl, clientId, mqttTopics, mqttQos);
			
			// 메시지 처리 콜백 생성 및 Bean 등록
			MqttMessageProcessor messageProcessor = new MqttMessageProcessor(
				globalEntityManager,
				scheduler,
				quartzCoreService,
				virtualTimeManager,
				eventPublisher,
				tcpServerTableModel,
				udpServerTableModel,
				timeMapRangeCompnents,
				slotStateManager,
				tsqMessageQueue,
				mqttClient
			);
			// MqttMessageProcessor를 Spring Bean으로 등록 (TsqEntityChangeStartDateQuartz에서 사용)
			BeanUtils.registerBean("mqttMessageProcessor", MqttMessageProcessor.class);
			// 등록된 Bean에 인스턴스 설정
			DefaultListableBeanFactory beanFactory = ApplicationContextProvider.getDefaultListableBeanFactory();
			if (beanFactory != null) {
				beanFactory.registerSingleton("mqttMessageProcessor", messageProcessor);
			}
			
			// MQTT 클라이언트 연결 및 구독
			boolean connected = mqttClient.connect(messageProcessor);
			if (connected) {
				log.info("MQTT client connected and subscribed successfully");
			} else {
				log.error("MQTT 클라이언트 연결 실패");
			}
		} catch (Exception e) {
			log.error("MQTT 클라이언트 시작 중 오류 발생: {}", e.getMessage(), e);
		}
	}
}

