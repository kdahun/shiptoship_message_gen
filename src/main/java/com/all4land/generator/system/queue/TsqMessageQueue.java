package com.all4land.generator.system.queue;

import com.all4land.generator.system.netty.dto.TsqResourceRequestMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TSQ 메시지 큐 관리 클래스
 * Thread-safe 큐를 사용하여 TSQ 요청 메시지를 저장하고 관리합니다.
 */
@Component
public class TsqMessageQueue {
	
	private final ConcurrentLinkedQueue<TsqResourceRequestMessage> queue = new ConcurrentLinkedQueue<>();
	
	/**
	 * TSQ 요청 메시지를 큐에 추가합니다.
	 * @param message TSQ 요청 메시지
	 */
	public void offer(TsqResourceRequestMessage message) {
		if (message != null) {
			queue.offer(message);
			System.out.println("[DEBUG] TSQ 메시지 큐에 추가 - Service: " + message.getService() + 
					", 큐 크기: " + queue.size());
		}
	}
	
	/**
	 * 큐의 첫 번째 TSQ 요청 메시지를 확인합니다 (제거하지 않음).
	 * @return TSQ 요청 메시지 (큐가 비어있으면 null)
	 */
	public TsqResourceRequestMessage peek() {
		return queue.peek();
	}
	
	/**
	 * 큐에서 TSQ 요청 메시지를 꺼냅니다.
	 * @return TSQ 요청 메시지 (큐가 비어있으면 null)
	 */
	public TsqResourceRequestMessage poll() {
		TsqResourceRequestMessage message = queue.poll();
		if (message != null) {
			System.out.println("[DEBUG] TSQ 메시지 큐에서 제거 - Service: " + message.getService() + 
					", 큐 크기: " + queue.size());
		}
		return message;
	}
	
	/**
	 * 큐가 비어있는지 확인합니다.
	 * @return 큐가 비어있으면 true
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	/**
	 * 큐의 크기를 반환합니다.
	 * @return 큐의 크기
	 */
	public int size() {
		return queue.size();
	}
	
	/**
	 * 큐의 모든 메시지를 제거합니다.
	 */
	public void clear() {
		queue.clear();
		System.out.println("[DEBUG] TSQ 메시지 큐 초기화 완료");
	}
}
