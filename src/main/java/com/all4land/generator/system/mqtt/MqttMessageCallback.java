package com.all4land.generator.system.mqtt;

/**
 * MQTT 메시지 수신 시 호출되는 콜백 인터페이스
 */
public interface MqttMessageCallback {
	
	/**
	 * MQTT 메시지를 수신했을 때 호출됩니다.
	 * @param topic 메시지가 수신된 토픽
	 * @param message 메시지 내용
	 */
	void onMessage(String topic, String message);
	
	/**
	 * 연결이 끊어졌을 때 호출됩니다.
	 * @param cause 연결 끊김 원인
	 */
	default void onConnectionLost(Throwable cause) {
		System.out.println("[DEBUG] ⚠️ MQTT 연결 끊김: " + (cause != null ? cause.getMessage() : "Unknown"));
	}
	
	/**
	 * 메시지 전송이 완료되었을 때 호출됩니다.
	 * @param token 메시지 토큰
	 */
	default void onDeliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
		// 기본 구현은 비어있음
	}
}

