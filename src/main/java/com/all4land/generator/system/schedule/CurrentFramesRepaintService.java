package com.all4land.generator.system.schedule;

import java.awt.Point;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.all4land.generator.ui.tab.ais.entity.CellInfos;
import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrentFramesRepaintService {
	//
	private final JTable currentFrameJTableNameUpper;
	private final JTable currentFrame1JTableNameUpper;
	private final JTable currentFrame2JTableNameUpper;
	private final JTable currentFrame3JTableNameUpper;
	private final JTable currentFrame4JTableNameUpper;
	private final JTable currentFrame5JTableNameUpper;
	private final JTable currentFrame6JTableNameUpper;
	private final JTable currentFrame7JTableNameUpper;
	private final JTable currentFrame8JTableNameUpper;
	private final JTable currentFrame9JTableNameUpper;
	private final JTable currentFrame10JTableNameUpper;
	
	private final JTable currentFrameJTableNameLower;
	private final JTable currentFrame1JTableNameLower;
	private final JTable currentFrame2JTableNameLower;
	private final JTable currentFrame3JTableNameLower;
	private final JTable currentFrame4JTableNameLower;
	private final JTable currentFrame5JTableNameLower;
	private final JTable currentFrame6JTableNameLower;
	private final JTable currentFrame7JTableNameLower;
	private final JTable currentFrame8JTableNameLower;
	private final JTable currentFrame9JTableNameLower;
	private final JTable currentFrame10JTableNameLower;
	
	CurrentFramesRepaintService(
			@Qualifier("currentFrameJTableNameUpper") JTable currentFrameJTableNameUpper
			, @Qualifier("currentFrame1JTableNameUpper") JTable currentFrame1JTableNameUpper
			, @Qualifier("currentFrame2JTableNameUpper") JTable currentFrame2JTableNameUpper
			, @Qualifier("currentFrame3JTableNameUpper") JTable currentFrame3JTableNameUpper
			, @Qualifier("currentFrame4JTableNameUpper") JTable currentFrame4JTableNameUpper
			, @Qualifier("currentFrame5JTableNameUpper") JTable currentFrame5JTableNameUpper
			, @Qualifier("currentFrame6JTableNameUpper") JTable currentFrame6JTableNameUpper
			, @Qualifier("currentFrame7JTableNameUpper") JTable currentFrame7JTableNameUpper
			, @Qualifier("currentFrame8JTableNameUpper") JTable currentFrame8JTableNameUpper
			, @Qualifier("currentFrame9JTableNameUpper") JTable currentFrame9JTableNameUpper
			, @Qualifier("currentFrame10JTableNameUpper") JTable currentFrame10JTableNameUpper
			, @Qualifier("currentFrameJTableNameLower") JTable currentFrameJTableNameLower
			, @Qualifier("currentFrame1JTableNameLower") JTable currentFrame1JTableNameLower
			, @Qualifier("currentFrame2JTableNameLower") JTable currentFrame2JTableNameLower
			, @Qualifier("currentFrame3JTableNameLower") JTable currentFrame3JTableNameLower
			, @Qualifier("currentFrame4JTableNameLower") JTable currentFrame4JTableNameLower
			, @Qualifier("currentFrame5JTableNameLower") JTable currentFrame5JTableNameLower
			, @Qualifier("currentFrame6JTableNameLower") JTable currentFrame6JTableNameLower
			, @Qualifier("currentFrame7JTableNameLower") JTable currentFrame7JTableNameLower
			, @Qualifier("currentFrame8JTableNameLower") JTable currentFrame8JTableNameLower
			, @Qualifier("currentFrame9JTableNameLower") JTable currentFrame9JTableNameLower
			, @Qualifier("currentFrame10JTableNameLower") JTable currentFrame10JTableNameLower
			) {
		//
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
		this.currentFrame8JTableNameUpper = currentFrame8JTableNameUpper;
		this.currentFrame9JTableNameUpper = currentFrame9JTableNameUpper;
		this.currentFrame10JTableNameUpper = currentFrame10JTableNameUpper;
		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
		this.currentFrame1JTableNameLower = currentFrame1JTableNameLower;
		this.currentFrame2JTableNameLower = currentFrame2JTableNameLower;
		this.currentFrame3JTableNameLower = currentFrame3JTableNameLower;
		this.currentFrame4JTableNameLower = currentFrame4JTableNameLower;
		this.currentFrame5JTableNameLower = currentFrame5JTableNameLower;
		this.currentFrame6JTableNameLower = currentFrame6JTableNameLower;
		this.currentFrame7JTableNameLower = currentFrame7JTableNameLower;
		this.currentFrame8JTableNameLower = currentFrame8JTableNameLower;
		this.currentFrame9JTableNameLower = currentFrame9JTableNameLower;
		this.currentFrame10JTableNameLower = currentFrame10JTableNameLower;
	}
	
//	@Scheduled(cron = "*/10 * * * * *", zone = "Asia/Seoul")
	@Scheduled(cron = "0 0/1 * * * *", zone = "Asia/Seoul") // 1분마다
	public void repaintFrames() {
		//
		log.info("Frame shift paint start.");
		this.repaintAllTable();
	}
	
	private void repaintAllTable() {
		//
		CompletableFuture<Map<Point, CellInfos>> upperfuture10 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame10JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture9 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame9JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture8 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame8JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture7 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame7JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture6 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame6JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture5 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame5JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture4 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame4JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture3 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame3JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture2 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame2JTableNameUpper); });
		
		CompletableFuture<Map<Point, CellInfos>> upperfuture1 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame1JTableNameUpper); });
		// ================================================================================
		CompletableFuture<Map<Point, CellInfos>> lowerfuture10 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame10JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture9 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame9JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture8 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame8JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture7 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame7JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture6 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame6JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture5 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame5JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture4 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame4JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture3 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame3JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture2 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame2JTableNameLower); });
		
		CompletableFuture<Map<Point, CellInfos>> lowerfuture1 = CompletableFuture.supplyAsync(() -> {
		    return this.getRepaintEntityLists(this.currentFrame1JTableNameLower); });
		
		
		// 작업이 완료되면 결과를 얻기
		Map<Point, CellInfos> lowerresultList10 = lowerfuture10.join();
		Map<Point, CellInfos> lowerresultList9 = lowerfuture9.join();
		Map<Point, CellInfos> lowerresultList8 = lowerfuture8.join();
		Map<Point, CellInfos> lowerresultList7 = lowerfuture7.join();
		Map<Point, CellInfos> lowerresultList6 = lowerfuture6.join();
		Map<Point, CellInfos> lowerresultList5 = lowerfuture5.join();
		Map<Point, CellInfos> lowerresultList4 = lowerfuture4.join();
		Map<Point, CellInfos> lowerresultList3 = lowerfuture3.join();
		Map<Point, CellInfos> lowerresultList2 = lowerfuture2.join();
		Map<Point, CellInfos> lowerresultList1 = lowerfuture1.join();
		
		Map<Point, CellInfos> upperresultList10 = upperfuture10.join();
		Map<Point, CellInfos> upperresultList9 = upperfuture9.join();
		Map<Point, CellInfos> upperresultList8 = upperfuture8.join();
		Map<Point, CellInfos> upperresultList7 = upperfuture7.join();
		Map<Point, CellInfos> upperresultList6 = upperfuture6.join();
		Map<Point, CellInfos> upperresultList5 = upperfuture5.join();
		Map<Point, CellInfos> upperresultList4 = upperfuture4.join();
		Map<Point, CellInfos> upperresultList3 = upperfuture3.join();
		Map<Point, CellInfos> upperresultList2 = upperfuture2.join();
		Map<Point, CellInfos> upperresultList1 = upperfuture1.join();
		
		CompletableFuture<Void> allOfUpper = CompletableFuture.allOf(upperfuture10, upperfuture9, upperfuture8, upperfuture7, upperfuture6, upperfuture5, upperfuture4, upperfuture3, upperfuture2, upperfuture1);
		CompletableFuture<Void> allOfLower = CompletableFuture.allOf(lowerfuture10, lowerfuture9, lowerfuture8, lowerfuture7, lowerfuture6, lowerfuture5, lowerfuture4, lowerfuture3, lowerfuture2, lowerfuture1);

		allOfUpper.join(); // 모든 작업이 완료될 때까지 대기
		allOfLower.join();
		
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrameJTableNameUpper, upperresultList1));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame1JTableNameUpper, upperresultList2));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame2JTableNameUpper, upperresultList3));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame3JTableNameUpper, upperresultList4));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame4JTableNameUpper, upperresultList5));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame5JTableNameUpper, upperresultList6));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame6JTableNameUpper, upperresultList7));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame7JTableNameUpper, upperresultList8));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame8JTableNameUpper, upperresultList9));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame9JTableNameUpper, upperresultList10));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame10JTableNameUpper, true));
		
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrameJTableNameLower, lowerresultList1));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame1JTableNameLower, lowerresultList2));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame2JTableNameLower, lowerresultList3));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame3JTableNameLower, lowerresultList4));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame4JTableNameLower, lowerresultList5));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame5JTableNameLower, lowerresultList6));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame6JTableNameLower, lowerresultList7));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame7JTableNameLower, lowerresultList8));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame8JTableNameLower, lowerresultList9));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame9JTableNameLower, lowerresultList10));
		CompletableFuture.runAsync(() -> this.repaintTable(this.currentFrame10JTableNameLower, false));
		
	}
	
	private void repaintTable(JTable targetTable, Map<Point, CellInfos> cellInfos) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) targetTable.getDefaultRenderer(Object.class);
		// 모든 셀 정보 클리어
		renderer.clearAllCellInfos();
		
		renderer.setCellInfos(cellInfos);
		targetTable.repaint();
    }
	
	private Map<Point, CellInfos> getRepaintEntityLists(JTable sourceTable){
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) sourceTable.getDefaultRenderer(Object.class);
		return renderer.getCellInfos();
	}
	
	private void repaintTable(JTable targetTable, boolean legType) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) targetTable.getDefaultRenderer(Object.class);
//		renderer.setInit(legType);
		renderer.clearAllCellInfos();
		targetTable.repaint();
    }
	
}
