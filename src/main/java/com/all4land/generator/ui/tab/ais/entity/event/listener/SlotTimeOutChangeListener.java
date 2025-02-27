package com.all4land.generator.ui.tab.ais.entity.event.listener;

import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.ui.tab.ais.entity.event.change.SlotTimeOutChangeEvent;
import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SlotTimeOutChangeListener {
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
	public void onEventListener(SlotTimeOutChangeEvent event) {
		//
		MmsiEntity mmsiEntity = event.getMmsiEntity();

		this.updateCurrentFrameTableNames(event);

		CompletableFuture.runAsync(() -> this.changeCellSlotTimeOutText(this.currentFrame7JTableNameUpper, mmsiEntity));
		CompletableFuture.runAsync(() -> this.changeCellSlotTimeOutText(this.currentFrame6JTableNameUpper, mmsiEntity));
		CompletableFuture.runAsync(() -> this.changeCellSlotTimeOutText(this.currentFrame5JTableNameUpper, mmsiEntity));
		CompletableFuture.runAsync(() -> this.changeCellSlotTimeOutText(this.currentFrame4JTableNameUpper, mmsiEntity));
		CompletableFuture.runAsync(() -> this.changeCellSlotTimeOutText(this.currentFrame3JTableNameUpper, mmsiEntity));
		CompletableFuture.runAsync(() -> this.changeCellSlotTimeOutText(this.currentFrame2JTableNameUpper, mmsiEntity));
		CompletableFuture.runAsync(() -> this.changeCellSlotTimeOutText(this.currentFrame1JTableNameUpper, mmsiEntity));
		CompletableFuture.runAsync(() -> this.changeCellSlotTimeOutText(this.currentFrameJTableNameUpper, mmsiEntity));

	}

	private void changeCellSlotTimeOutText(JTable table, MmsiEntity mmsiEntity) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
		renderer.setSlotTimeOut(mmsiEntity);
		table.repaint();
	}

	private void updateCurrentFrameTableNames(SlotTimeOutChangeEvent event) {
		this.currentFrameJTableNameUpper = event.getCurrentFrameJTableNameUpper();
		this.currentFrame1JTableNameUpper = event.getCurrentFrame1JTableNameUpper();
		this.currentFrame2JTableNameUpper = event.getCurrentFrame2JTableNameUpper();
		this.currentFrame3JTableNameUpper = event.getCurrentFrame3JTableNameUpper();
		this.currentFrame4JTableNameUpper = event.getCurrentFrame4JTableNameUpper();
		this.currentFrame5JTableNameUpper = event.getCurrentFrame5JTableNameUpper();
		this.currentFrame6JTableNameUpper = event.getCurrentFrame6JTableNameUpper();
		this.currentFrame7JTableNameUpper = event.getCurrentFrame7JTableNameUpper();
	}

	public JTable getCurrentFrameJTableNameUpper() {
		return currentFrameJTableNameUpper;
	}

	public void setCurrentFrameJTableNameUpper(JTable currentFrameJTableNameUpper) {
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
	}

	public JTable getCurrentFrame1JTableNameUpper() {
		return currentFrame1JTableNameUpper;
	}

	public void setCurrentFrame1JTableNameUpper(JTable currentFrame1JTableNameUpper) {
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
	}

	public JTable getCurrentFrame2JTableNameUpper() {
		return currentFrame2JTableNameUpper;
	}

	public void setCurrentFrame2JTableNameUpper(JTable currentFrame2JTableNameUpper) {
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
	}

	public JTable getCurrentFrame3JTableNameUpper() {
		return currentFrame3JTableNameUpper;
	}

	public void setCurrentFrame3JTableNameUpper(JTable currentFrame3JTableNameUpper) {
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
	}

	public JTable getCurrentFrame4JTableNameUpper() {
		return currentFrame4JTableNameUpper;
	}

	public void setCurrentFrame4JTableNameUpper(JTable currentFrame4JTableNameUpper) {
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
	}

	public JTable getCurrentFrame5JTableNameUpper() {
		return currentFrame5JTableNameUpper;
	}

	public void setCurrentFrame5JTableNameUpper(JTable currentFrame5JTableNameUpper) {
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
	}

	public JTable getCurrentFrame6JTableNameUpper() {
		return currentFrame6JTableNameUpper;
	}

	public void setCurrentFrame6JTableNameUpper(JTable currentFrame6JTableNameUpper) {
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
	}

	public JTable getCurrentFrame7JTableNameUpper() {
		return currentFrame7JTableNameUpper;
	}

	public void setCurrentFrame7JTableNameUpper(JTable currentFrame7JTableNameUpper) {
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
	}

}
