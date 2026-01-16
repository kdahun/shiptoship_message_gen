package com.all4land.generator.system.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

/**
 * MQTT 클라이언트 설정 및 관리 클래스
 */
@Component
public class MqttClientConfiguration implements AutoCloseable {
	
	private MqttClient mqttClient;
	private String brokerUrl;
	private String clientId;
	private String[] topics;
	private int[] qos;
	
	/**
	 * MQTT 클라이언트 초기화
	 * @param brokerUrl MQTT 브로커 URL (예: "tcp://localhost:1883")
	 * @param clientId 클라이언트 ID (고유해야 함)
	 * @param topics 구독할 토픽 배열
	 * @param qos 각 토픽의 QoS 레벨 배열 (0, 1, 2)
	 */
	public void initialize(String brokerUrl, String clientId, String[] topics, int[] qos) {
		this.brokerUrl = brokerUrl;
		this.clientId = clientId;
		this.topics = topics;
		this.qos = qos;
	}
	
	/**
	 * MQTT 클라이언트 생성 및 연결
	 * @param callback 메시지 수신 시 호출될 콜백
	 * @return 연결 성공 여부
	 */
	public boolean connect(MqttMessageCallback callback) {
		try {
			// MQTT 클라이언트 생성 (메모리 영속성 사용)
			this.mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
			
			// 연결 옵션 설정
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);  // 클린 세션 사용
			options.setAutomaticReconnect(true);  // 자동 재연결 활성화
			options.setConnectionTimeout(30);  // 연결 타임아웃 30초
			options.setKeepAliveInterval(60);  // Keep-Alive 간격 60초
			
			// 연결
			System.out.println("[DEBUG] MQTT 브로커 연결 중: " + brokerUrl);
			mqttClient.connect(options);
			System.out.println("[DEBUG] ✅ MQTT 브로커 연결 성공: " + brokerUrl);
			
			// 콜백 설정
			if (callback != null) {
				mqttClient.setCallback(new MqttMessageHandler(callback));
			}
			
			// 토픽 구독
			if (topics != null && topics.length > 0) {
				subscribe(topics, qos);
			}
			
			return true;
		} catch (MqttException e) {
			System.out.println("[DEBUG] ❌ MQTT 브로커 연결 실패: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 토픽 구독
	 * @param topics 구독할 토픽 배열
	 * @param qos 각 토픽의 QoS 레벨 배열
	 */
	public void subscribe(String[] topics, int[] qos) {
		if (mqttClient == null || !mqttClient.isConnected()) {
			System.out.println("[DEBUG] ⚠️ MQTT 클라이언트가 연결되지 않았습니다.");
			return;
		}
		
		try {
			mqttClient.subscribe(topics, qos);
			System.out.println("[DEBUG] ✅ MQTT 토픽 구독 완료:");
			for (int i = 0; i < topics.length; i++) {
				System.out.println("[DEBUG]   - 토픽: " + topics[i] + " (QoS: " + qos[i] + ")");
			}
		} catch (MqttException e) {
			System.out.println("[DEBUG] ❌ MQTT 토픽 구독 실패: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 토픽 구독 해제
	 * @param topics 구독 해제할 토픽 배열
	 */
	public void unsubscribe(String[] topics) {
		if (mqttClient == null || !mqttClient.isConnected()) {
			return;
		}
		
		try {
			mqttClient.unsubscribe(topics);
			System.out.println("[DEBUG] MQTT 토픽 구독 해제 완료");
		} catch (MqttException e) {
			System.out.println("[DEBUG] ❌ MQTT 토픽 구독 해제 실패: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * MQTT 메시지 발행
	 * @param topic 토픽
	 * @param message 메시지 내용
	 * @param qos QoS 레벨
	 * @param retained retained 플래그
	 */
	public void publish(String topic, String message, int qos, boolean retained) {
		if (mqttClient == null || !mqttClient.isConnected()) {
			System.out.println("[DEBUG] ⚠️ MQTT 클라이언트가 연결되지 않았습니다.");
			return;
		}
		
		try {
			org.eclipse.paho.client.mqttv3.MqttMessage mqttMessage = 
				new org.eclipse.paho.client.mqttv3.MqttMessage(message.getBytes());
			mqttMessage.setQos(qos);
			mqttMessage.setRetained(retained);
			
			mqttClient.publish(topic, mqttMessage);
			System.out.println("[DEBUG] ✅ MQTT 메시지 발행 완료: " + topic);
		} catch (MqttException e) {
			System.out.println("[DEBUG] ❌ MQTT 메시지 발행 실패: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 연결 상태 확인
	 * @return 연결 여부
	 */
	public boolean isConnected() {
		return mqttClient != null && mqttClient.isConnected();
	}
	
	/**
	 * 연결 해제
	 */
	public void disconnect() {
		if (mqttClient != null && mqttClient.isConnected()) {
			try {
				mqttClient.disconnect();
				System.out.println("[DEBUG] MQTT 브로커 연결 해제 완료");
			} catch (MqttException e) {
				System.out.println("[DEBUG] ❌ MQTT 브로커 연결 해제 실패: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void close() throws Exception {
		disconnect();
		if (mqttClient != null) {
			try {
				mqttClient.close();
			} catch (MqttException e) {
				System.out.println("[DEBUG] ❌ MQTT 클라이언트 종료 실패: " + e.getMessage());
			}
		}
	}
}

