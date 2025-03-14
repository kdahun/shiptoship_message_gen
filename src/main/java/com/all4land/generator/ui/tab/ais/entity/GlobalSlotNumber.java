package com.all4land.generator.ui.tab.ais.entity;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.all4land.generator.system.component.TimeMapRangeCompnents;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.ui.tab.ais.entity.event.change.ToggleSlotNumberEvent;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GlobalSlotNumber {
    private static final long DEFAULT_SLEEP_TIME_MS = 15; // 기본 대기 시간
    private final ApplicationEventPublisher eventPublisher;
    private final TimeMapRangeCompnents timeMapRangeCompnents;
    private final JLabel jLabelSlotNumber;
    private final ExecutorService executorService;

    private int slotNumber = 0;
    private long sleepTimeMs = DEFAULT_SLEEP_TIME_MS;

    public GlobalSlotNumber(ApplicationEventPublisher eventPublisher,
                            TimeMapRangeCompnents timeMapRangeCompnents,
                            @Qualifier("jLabelSlotNumber") JLabel jLabelSlotNumber) {
        this.eventPublisher = eventPublisher;
        this.timeMapRangeCompnents = timeMapRangeCompnents;
        this.jLabelSlotNumber = jLabelSlotNumber;
        this.executorService = Executors.newSingleThreadExecutor(); // 스레드 풀 생성
    }

    public String getSlotNumberString() {
        return String.valueOf(this.slotNumber);
    }

    public synchronized int getSlotNumber() {
        return this.slotNumber;
    }

    public synchronized void setSlotNumber(int slotNumber) {
        if (this.slotNumber != slotNumber) {
            this.slotNumber = slotNumber;

            // 스윙 UI 업데이트는 별도의 스레드에서 실행 시 오류가 생길 가능성이 매우 높아짐
            // 따라서 스윙 UI 스레드(Event Dispacther Thread)에서 처리할 수 있도록 변경
            SwingUtilities.invokeLater(()-> this.jLabelSlotNumber.setText(String.valueOf(slotNumber)));
            // log.info("슬롯 번호 변경됨: {}", slotNumber);
            eventPublisher.publishEvent(new ToggleSlotNumberEvent(this, this));
        }
    }

    @PostConstruct
    public void startThread() {
        executorService.submit(this::process); // 스레드 풀에서 실행
    }

    private void process() {
    	//
        while (!Thread.currentThread().isInterrupted()) {
            try {
                boolean slotChanged = updateSlotNumber(); // 슬롯 번호 업데이트 여부 확인
                
                // 슬롯 번호가 변경되었을 때만 대기 시간을 적용
                if (slotChanged) {
                    Thread.sleep(sleepTimeMs);
                }
            } catch (InterruptedException e) {
                log.error("슬롯 번호 업데이트 스레드가 중단됨.", e);
                Thread.currentThread().interrupt(); // 인터럽트 상태 복원
            }
        }
    }

    /**
     * 슬롯 번호를 업데이트하고, 변경 여부를 반환.
     * 
     * @return true if the slot number changed, false otherwise
     */
    private boolean updateSlotNumber() {
        LocalDateTime now = LocalDateTime.now();
        String formatNow = now.format(SystemConstMessage.formatterForStartIndex);

        // 현재 시간의 초(ss.SSSS)를 기준으로 몇번 슬롯에 들어가면 될지 검색 
        int newSlotNumber = timeMapRangeCompnents.findSlotNumber(formatNow);
        if (newSlotNumber != -1 && this.slotNumber != newSlotNumber) {
        	//
            setSlotNumber(newSlotNumber);  // 슬롯 번호 변경 및 이벤트 발생
//            Range v = timeMapRangeCompnents.getRange(newSlotNumber);
//            log.info("OS 시간: {}, 차이 :{}, 새로운 슬롯 번호: {}", formatNow, Double.valueOf(formatNow) - v.getFrom(),newSlotNumber);
            return true; // 슬롯 번호가 변경되었음
        }
        return false; // 슬롯 번호가 변경되지 않음
    }
}



//@Slf4j
//@Component
//public class GlobalSlotNumber {
//	//
//	private static final long SLEEP_TIME_MS = 15; // 루프 사이의 대기 시간 (밀리초 단위)
//	
//	private final ApplicationEventPublisher eventPublisher;
//	private final TimeMapRangeCompnents timeMapRangeCompnents;
//	private final  JLabel jLabelSlotNumber;
//	private int slotNumber = 0;
//	
//	public GlobalSlotNumber(ApplicationEventPublisher eventPublisher
//			, TimeMapRangeCompnents timeMapRangeCompnents
//			, @Qualifier("jLabelSlotNumber") JLabel jLabelSlotNumber) {
//		//
//		this.eventPublisher = eventPublisher;
//		this.jLabelSlotNumber = jLabelSlotNumber;
//		this.timeMapRangeCompnents = timeMapRangeCompnents;
//	}
//	
//	public String getSlotNumberString() {
//		//
//		return String.valueOf(this.slotNumber);
//	}
//
//	public int getSlotNumber() {
//		return this.slotNumber;
//	}
//
//	public void setSlotNumber(int slotNumber) {
//		//
//		ToggleSlotNumberEvent event = new ToggleSlotNumberEvent(this, this);
//		this.slotNumber = slotNumber;
//		this.jLabelSlotNumber.setText(String.valueOf(slotNumber));
//		this.eventPublisher.publishEvent(event);
//	}
//
//	@PostConstruct
//    public void startThread() {
//		//
////		CompletableFuture.runAsync(() -> this.process());
//		Thread thread = new Thread(this::process);  // process() 메서드를 새 스레드에서 실행
//	    thread.start();  // 스레드를 시작
//    }
//	
//	private void process() {
//		//
//		while(true) {
//			//
//			LocalDateTime now = LocalDateTime.now();
//			String formatNow = now.format(SystemConstMessage.formatterForStartIndex);
//			
//			int findSlotNumber = this.timeMapRangeCompnents.findSlotNumber(formatNow);
//			
//			if(this.slotNumber != findSlotNumber && findSlotNumber != -1) {
//				// 슬롯이 변화가 생겼다
//				this.slotNumber = findSlotNumber;
//				System.out.println("OS 시간 1 : "+ formatNow + ", "+ this.slotNumber);
//				try {
//	                Thread.sleep(SLEEP_TIME_MS);
//	            } catch (InterruptedException e) {
//	                Thread.currentThread().interrupt();
//	                break;
//	            }
//			}
//		}
//	}
//}
