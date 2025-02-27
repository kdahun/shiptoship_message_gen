package com.all4land.generator.ui.tab.ais.entity.event.listener;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.ui.tab.ais.entity.event.change.ColorEntityChangeEvent;
import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

@Component
public class ColorEntityChangeListener {
	//
	private JTable currentFrameJTableNameUpper;
	private JTable currentFrame1JTableNameUpper;
	private JTable currentFrame2JTableNameUpper;
	private JTable currentFrame3JTableNameUpper;
	private JTable currentFrame4JTableNameUpper;
	private JTable currentFrame5JTableNameUpper;
	private JTable currentFrame6JTableNameUpper;
	private JTable currentFrame7JTableNameUpper;

	@EventListener
	public void onEventListener(ColorEntityChangeEvent event) {
		//
		MmsiEntity mmsiEntity = event.getMmsiEntity();
		LocalDateTime time = event.getTime();
		int row = event.getRow();
		int col = event.getCol();
		char channel = event.getChannel();

		this.updateCurrentFrameTableNames(event);
		
		int slotTimeOut = mmsiEntity.getSlotTimeOut();
		switch (slotTimeOut) {
		case 0:
			//
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
//			this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time);
			break;
		case 1:
			//
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
//			this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time);
			break;
		case 2:
			//
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
//			this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time);

			break;
		case 3:
			//
			if(mmsiEntity.getSpeed() == 180) {
				CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time));
				CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
			}else {
				CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time));
				CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time));
				CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time));
				CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
			}
//			this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time);
			break;
		case 4:
			//
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame4JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
//			this.changeCellColors(this.currentFrame4JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time);

			break;
		case 5:
			//
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame5JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame4JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
//			this.changeCellColors(this.currentFrame5JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame4JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time);
			break;
		case 6:
			//
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame6JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame5JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame4JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
//			this.changeCellColors(this.currentFrame6JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame5JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame4JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time);
			break;
		case 7:
			//
//			this.changeCellColors(this.currentFrame7JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame6JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame5JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame4JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time);
//			this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time);
			
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame7JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame6JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame5JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame4JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame3JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame2JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrame1JTableNameUpper, row, col, mmsiEntity, channel, time));
			CompletableFuture.runAsync(() -> this.changeCellColors(this.currentFrameJTableNameUpper, row, col, mmsiEntity, channel, time));
			break;

		default:
			break;
		}

	}

	private void changeCellColors(JTable table, int findRow, int findColumn,
			MmsiEntity mmsiEntity, char channel, LocalDateTime time) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
		renderer.setCellInfosAis(findRow, findColumn, mmsiEntity, channel, time);
		table.repaint();
	}

	private void updateCurrentFrameTableNames(ColorEntityChangeEvent event) {
		this.currentFrameJTableNameUpper = event.getCurrentFrameJTableNameUpper();
		this.currentFrame1JTableNameUpper = event.getCurrentFrame1JTableNameUpper();
		this.currentFrame2JTableNameUpper = event.getCurrentFrame2JTableNameUpper();
		this.currentFrame3JTableNameUpper = event.getCurrentFrame3JTableNameUpper();
		this.currentFrame4JTableNameUpper = event.getCurrentFrame4JTableNameUpper();
		this.currentFrame5JTableNameUpper = event.getCurrentFrame5JTableNameUpper();
		this.currentFrame6JTableNameUpper = event.getCurrentFrame6JTableNameUpper();
		this.currentFrame7JTableNameUpper = event.getCurrentFrame7JTableNameUpper();
	}

	public JTable getCurrentFrameJTableName() {
		return currentFrameJTableNameUpper;
	}

	public void setCurrentFrameJTableName(JTable currentFrameJTableName) {
		this.currentFrameJTableNameUpper = currentFrameJTableName;
	}

	public JTable getCurrentFrame1JTableName() {
		return currentFrame1JTableNameUpper;
	}

	public void setCurrentFrame1JTableName(JTable currentFrame1JTableName) {
		this.currentFrame1JTableNameUpper = currentFrame1JTableName;
	}

	public JTable getCurrentFrame2JTableName() {
		return currentFrame2JTableNameUpper;
	}

	public void setCurrentFrame2JTableName(JTable currentFrame2JTableName) {
		this.currentFrame2JTableNameUpper = currentFrame2JTableName;
	}

	public JTable getCurrentFrame3JTableName() {
		return currentFrame3JTableNameUpper;
	}

	public void setCurrentFrame3JTableName(JTable currentFrame3JTableName) {
		this.currentFrame3JTableNameUpper = currentFrame3JTableName;
	}

	public JTable getCurrentFrame4JTableName() {
		return currentFrame4JTableNameUpper;
	}

	public void setCurrentFrame4JTableName(JTable currentFrame4JTableName) {
		this.currentFrame4JTableNameUpper = currentFrame4JTableName;
	}

	public JTable getCurrentFrame5JTableName() {
		return currentFrame5JTableNameUpper;
	}

	public void setCurrentFrame5JTableName(JTable currentFrame5JTableName) {
		this.currentFrame5JTableNameUpper = currentFrame5JTableName;
	}

	public JTable getCurrentFrame6JTableName() {
		return currentFrame6JTableNameUpper;
	}

	public void setCurrentFrame6JTableName(JTable currentFrame6JTableName) {
		this.currentFrame6JTableNameUpper = currentFrame6JTableName;
	}
	
	public JTable getCurrentFrame7JTableName() {
		return currentFrame7JTableNameUpper;
	}

	public void setCurrentFrame7JTableName(JTable currentFrame7JTableName) {
		this.currentFrame7JTableNameUpper = currentFrame7JTableName;
	}

}
