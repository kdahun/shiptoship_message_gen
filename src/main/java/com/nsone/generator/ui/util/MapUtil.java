package com.nsone.generator.ui.util;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.nsone.generator.ui.tab.ais.entity.CellInfos;

public class MapUtil {
	//
	// 깊은 복사를 수행하는 유틸리티 메서드
	public static void copy(Map<Point, CellInfos> cellInfos, Map<Point, CellInfos> cellInfosDefault) {
		//
		for (Entry<Point, CellInfos> entry : cellInfosDefault.entrySet()) {
            cellInfos.put(entry.getKey(), new CellInfos(entry.getValue()));
        }
	}
	
	public static Map<Point, CellInfos> copy(Map<Point, CellInfos> cellInfos) {
		//
		Map<Point, CellInfos> cellInfosReturn = new LinkedHashMap<>();
		for (Entry<Point, CellInfos> entry : cellInfos.entrySet()) {
			cellInfosReturn.put(entry.getKey(), new CellInfos(entry.getValue()));
        }
		return cellInfosReturn;
	}
}

