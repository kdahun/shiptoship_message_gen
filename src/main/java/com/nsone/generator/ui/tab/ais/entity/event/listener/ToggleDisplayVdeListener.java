package com.nsone.generator.ui.tab.ais.entity.event.listener;

import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nsone.generator.ui.tab.ais.entity.event.change.ToggleDisplayVde;
import com.nsone.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

@Component
public class ToggleDisplayVdeListener {
	//
	@EventListener
	public void onEventListener(ToggleDisplayVde event) {
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
		
		JTable currentFrameJTableNameLower = event.getCurrentFrameJTableNameLower();
		JTable currentFrame1JTableNameLower = event.getCurrentFrame1JTableNameLower();
		JTable currentFrame2JTableNameLower = event.getCurrentFrame2JTableNameLower();
		JTable currentFrame3JTableNameLower = event.getCurrentFrame3JTableNameLower();
		JTable currentFrame4JTableNameLower = event.getCurrentFrame4JTableNameLower();
		JTable currentFrame5JTableNameLower = event.getCurrentFrame5JTableNameLower();
		JTable currentFrame6JTableNameLower = event.getCurrentFrame6JTableNameLower();
		JTable currentFrame7JTableNameLower = event.getCurrentFrame7JTableNameLower();
		JTable currentFrame8JTableNameLower = event.getCurrentFrame8JTableNameLower();
		JTable currentFrame9JTableNameLower = event.getCurrentFrame9JTableNameLower();
		JTable currentFrame10JTableNameLower = event.getCurrentFrame10JTableNameLower();
		
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrameJTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame1JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame2JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame3JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame4JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame5JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame6JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame7JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame8JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame9JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame10JTableNameUpper, bool));
		
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrameJTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame1JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame2JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame3JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame4JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame5JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame6JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame7JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame8JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame9JTableNameLower, bool));
		CompletableFuture.runAsync(() -> this.changeVdeDisplayToogle(currentFrame10JTableNameLower, bool));
		
	}
	
	private void changeVdeDisplayToogle(JTable table, boolean bool) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
		renderer.setVdeDisplayToggle(bool);
		table.repaint();
	}
}
