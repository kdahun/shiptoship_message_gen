package com.all4land.generator.system.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * MQTT 메시지 핸들러
 * MQTT 클라이언트의 콜백을 처리합니다.
 */
public class MqttMessageHandler implements MqttCallback {
	
	private final MqttMessageCallback callback;
	
	public MqttMessageHandler(MqttMessageCallback callback) {
		this.callback = callback;
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		callback.onConnectionLost(cause);
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String payload = new String(message.getPayload());
		callback.onMessage(topic, payload);
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		callback.onDeliveryComplete(token);
	}
}

