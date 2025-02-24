package com.nsone.generator.ui.tab.ais.entity.event.listener;

import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nsone.generator.ui.tab.ais.entity.event.change.ToggleDisplayAsm;
import com.nsone.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

@Component
public class ToggleDisplayAsmListener {
	//
	@EventListener
	public void onEventListener(ToggleDisplayAsm event) {
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
		
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrameJTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame1JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame2JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame3JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame4JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame5JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame6JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame7JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame8JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame9JTableNameUpper, bool));
		CompletableFuture.runAsync(() -> this.changeAsmDisplayToogle(currentFrame10JTableNameUpper, bool));
		
	}
	
	private void changeAsmDisplayToogle(JTable table, boolean bool) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
		renderer.setAsmDisplayToggle(bool);
		table.repaint();
	}
}
