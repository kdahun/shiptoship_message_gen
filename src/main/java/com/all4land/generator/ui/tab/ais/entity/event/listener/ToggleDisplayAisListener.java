package com.all4land.generator.ui.tab.ais.entity.event.listener;

import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.all4land.generator.ui.tab.ais.entity.event.change.ToggleDisplayAis;
import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

@Component
public class ToggleDisplayAisListener {
	//
	@EventListener
	public void onEventListener(ToggleDisplayAis event) {
		//
		boolean bool = event.isBool();
		JTable currentFrameJTableNameUpper = event.getCurrentFrameJTableNameUpper();
		JTable currentFrame1JTableNameUpper = event.getCurrentFrame1JTableNameUpper();
		JTable currentFrame2JTableNameUpper = event.getCurrentFrame2JTableNameUpper();
		JTable currentFrame3JTableNameUpper = event.getCurrentFrame3JTableNameUpper();
		JTable currentFrame4JTableNameUpper = event.getCurrentFrame4JTableNameUpper();
		JTable currentFrame5JTableNameUpper = event.getCurrentFrame5JTableNameUpper();
		JTable currentFrame6JTableNameUpper = event.getCurrentFrame6JTableNameUpper();
		JTable currentFrame7JTableNameUpper = event.getCurrentFrame7JTableNameUpper();
		JTable currentFrame8JTableNameUpper = event.getCurrentFrame8JTableNameUpper();
		JTable currentFrame9JTableNameUpper = event.getCurrentFrame9JTableNameUpper();
		JTable currentFrame10JTableNameUpper = event.getCurrentFrame10JTableNameUpper();
		
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrameJTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame1JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame2JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame3JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame4JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame5JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame6JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame7JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame8JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame9JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAisDisplayToogle(currentFrame10JTableNameUpper, bool));
		
	}
	
	private void changeAisDisplayToogle(JTable table, boolean bool) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
		renderer.setAisDisplayToggle(bool);
		table.repaint();
	}
}
