//package com.all4land.generator;
//
//import java.time.LocalDateTime;
//
//import com.all4land.generator.system.constant.SystemConstMessage;
//import com.all4land.generator.ui.tab.ais.entity.event.change.ToggleSlotNumberEvent;
//
//public class SlotThreadClacu {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private static int slotNumber;
//
//	private void process() {
//		//
//		while(true) {
//			//
//			LocalDateTime now = LocalDateTime.now();
//			String formatNow = now.format(SystemConstMessage.formatterForStartIndex);
//			
//			int findSlotNumber = this.timeMapRangeCompnents.findStartSlotNumber(formatNow);
//			
//			System.out.println("OS 시간 : "+ formatNow);
//			
//			if(this.slotNumber != findSlotNumber && findSlotNumber != -1) {
//				//
//				this.setSlotNumber(findSlotNumber);
//				if(this.slotNumber >= 0 && this.slotNumber <= 1 || this.slotNumber >= 2248 && this.slotNumber <= 2249) {
//					System.out.println("시작시간 : "+ now+", 현재 슬롯: "+this.slotNumber);
//				}
//				
//				try {
//	                Thread.sleep(SLEEP_TIME_MS);
//	            } catch (InterruptedException e) {
//	                Thread.currentThread().interrupt();
//	                break;
//	            }
//			}
//		}
//	}
//	
//	private static void setSlotNumber(int slotNumberValue) {
//		//
////		ToggleSlotNumberEvent event = new ToggleSlotNumberEvent(this, this);
//		slotNumber = slotNumberValue;
////		this.jLabelSlotNumber.setText(String.valueOf(slotNumber));
////		this.eventPublisher.publishEvent(event);
//	}
//}
