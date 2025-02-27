package com.all4land.generator.system.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.all4land.generator.ui.tab.ais.entity.Range;

@Component
public class TimeMapRangeCompnents {
	//
	List<Range> range = new ArrayList<>();
	Map<Integer, Range> rangeMap = new HashMap<>();
	
	public TimeMapRangeCompnents() {
		// TODO Auto-generated constructor stub
		double divisor = 0.0266666666666667; 
		double currentSecond = 59.999999999999;
		//double epsilon = 0.0001; // 최소한의 작은값
		
    	int tmp = (int) (currentSecond / divisor);
    	for(int i = 0 ; i <= tmp ; i++) {
    		//
    		double from = i * divisor;
            double to = (i == tmp) ? currentSecond : (i + 1) * divisor;// - epsilon;
            range.add(new Range(from, to, i));
    	}
    	
//    	range.forEach(System.out::println);
    	
    	for (Range range : this.range) {
            this.rangeMap.put(range.getSlotNumber(), range);
        }
    	
//    	int a = findStartSlotNumber("14.9600");
////    	int aa = findStartSlotNumber("24.8260");
//    	System.out.println(a);
	}
	
	// 이진 검색
	public int findStartSlotNumber(String ssSSSS) {
		//
		// 시간 측정 시작
//	    long startTime = System.nanoTime();  // 나노초 단위로 시작 시간 기록
		
	    double value = Double.valueOf(ssSSSS);
	    int low = 0;
	    int high = range.size() - 1;

	    while (low <= high) {
	        int mid = low + (high - low) / 2;
	        Range r = range.get(mid);

	        if (value >= r.getFrom() && value <= r.getTo()) {
	        	//
	        	// 시간 측정 종료
//	            long endTime = System.nanoTime();  // 나노초 단위로 종료 시간 기록
//	            long duration = endTime - startTime;  // 소요 시간 계산

//	            System.out.println("메서드 실행 시간: " + duration + " 나노초 (" + (duration / 1_000_000.0) + " 밀리초)");
	        	
	            return mid;
	        } else if (value < r.getFrom()) {
	            high = mid - 1;
	        } else {
	            low = mid + 1;
	        }
	    }

	    return -1; // 찾지 못한 경우
	}
	
	public double findNextssSSSS(String ssSSSS) {
	    double value = Double.valueOf(ssSSSS);
	    int low = 0;
	    int high = range.size() - 1;

	    while (low <= high) {
	        int mid = low + (high - low) / 2;
	        Range r = range.get(mid);

	        if (value >= r.getFrom() && value <= r.getTo()) {
	            return range.get(mid+1).getFrom();//r.getTo();
	        } else if (value < r.getFrom()) {
	            high = mid - 1;
	        } else {
	            low = mid + 1;
	        }
	    }

	    return -1; // 찾지 못한 경우
	}
	
	public List<Double> getFromArray(double cut) {
		//
		List<Double> from = new ArrayList<>();
		for(int i = 0; i < this.range.size(); i++) {
			//
			if(cut <= this.range.get(i).getFrom()) {
				//
				from.add(this.range.get(i).getFrom());
			}
		}
		return from;
	}
	
	public double getNextssSSSS(int slotNumber) {
		//
//		int targetSlotNumber = slotNumber;
//		if(slotNumber+1 >= 2250) {
//			//
//			return this.rangeMap.get(0).getFrom();
//		}else {
			//
			//System.out.println(slotNumber);
			double v = this.rangeMap.get(slotNumber).getFrom()+0.005;
			//System.out.println("계산된 넘겨줄 시간 : "+ v);
			return v;
//		}
		
	}
	
	public Range getRange(int slotNumber) {
		//
		return this.rangeMap.get(slotNumber);
	}
}
