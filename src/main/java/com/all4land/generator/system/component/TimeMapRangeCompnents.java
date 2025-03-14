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
	
	/**
	 * 각 MmsiEntity마다 이 컴포넌트를 갖고 있다.
	 * range, rangeMap 초기화
	 * range : 2249개 슬롯의 각 시작시간, 종료시간, 슬롯번호를 담은 리스트
	 * rangeMap : 슬롯번호를 key로 하고 Range를 value로 하는 맵
	 */
	public TimeMapRangeCompnents() {
		// TODO Auto-generated constructor stub
		double divisor = 0.0266666666666667; 
		double currentSecond = 59.999999999999;
		
    	int tmp = (int) (currentSecond / divisor);
    	for(int i = 0 ; i <= tmp ; i++) {
    		//
    		double from = i * divisor;
            double to = (i == tmp) ? currentSecond : (i + 1) * divisor;
            range.add(new Range(from, to, i));
    	}
    	
    	for (Range range : this.range) {
            this.rangeMap.put(range.getSlotNumber(), range);
        }
	}
	
	/**
	 * 이진검색 알고리즘으로 해당 ssSSSS가 속한 슬롯번호를 찾는다.
	 * @param ssSSSS 시간
	 * @return 슬롯번호
	 */
	public int findSlotNumber(String ssSSSS) {
		//		
	    double value = Double.parseDouble(ssSSSS);
	    int low = 0;
	    int high = range.size() - 1;

		// 0 ~ 2249
	    while (low <= high) {
	        int mid = low + (high - low) / 2;
	        Range r = range.get(mid);

	        if (value >= r.getFrom() && value <= r.getTo()) {
	        	//
	            return mid;
	        } else if (value < r.getFrom()) {
	            high = mid - 1;
	        } else {
	            low = mid + 1;
	        }
	    }

	    return -1;
	}
	
	/**
	 * 해당 ssSSSS가 속한 슬롯의 다음 슬롯의 시작시간을 찾는다.
	 * @param ssSSSS 시간
	 * @return 다음 슬롯의 시작시간
	 */
	public double findNextssSSSS(String ssSSSS) {
		//
		int slotNumber = findSlotNumber(ssSSSS);
		return slotNumber == -1 ? 
			-1 : range.get(slotNumber+1).getFrom();
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
