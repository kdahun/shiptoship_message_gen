package com.all4land.generator.system.schedule;

import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OccupancyinformationService {
	//
	private final JProgressBar jProgressBarAisChannelA;
	private final JProgressBar jProgressBarAisChannelB;
	private final JProgressBar jProgressBarAsmChannelA;
	private final JProgressBar jProgressBarAsmChannelB;
	private final JProgressBar jProgressBarVdeUpper;
	private final JProgressBar jProgressBarVdeLower;
	private final JLabel jLabelAisChannelAoccupancyinformation;
	private final JLabel jLabelAisChannelBoccupancyinformation;
	private final JLabel jLabelAsmChannelAoccupancyinformation;
	private final JLabel jLabelAsmChannelBoccupancyinformation;
	private final JLabel jLabelVdeUpperoccupancyinformation;
	private final JLabel jLabelVdeLoweroccupancyinformation;
	
	private final JTable currentFrameJTableNameUpper;
	private final JTable currentFrameJTableNameLower;
	
	OccupancyinformationService(
			@Qualifier("jProgressBarAisChannelA") JProgressBar jProgressBarAisChannelA
			, @Qualifier("jProgressBarAisChannelB") JProgressBar jProgressBarAisChannelB
			, @Qualifier("jProgressBarAsmChannelA") JProgressBar jProgressBarAsmChannelA
			, @Qualifier("jProgressBarAsmChannelB") JProgressBar jProgressBarAsmChannelB
			, @Qualifier("jProgressBarVdeUpper") JProgressBar jProgressBarVdeUpper
			, @Qualifier("jProgressBarVdeLower") JProgressBar jProgressBarVdeLower
			//
			, @Qualifier("jLabelAisChannelAoccupancyinformation") JLabel jLabelAisChannelAoccupancyinformation
			, @Qualifier("jLabelAisChannelBoccupancyinformation") JLabel jLabelAisChannelBoccupancyinformation
			, @Qualifier("jLabelAsmChannelAoccupancyinformation") JLabel jLabelAsmChannelAoccupancyinformation
			, @Qualifier("jLabelAsmChannelBoccupancyinformation") JLabel jLabelAsmChannelBoccupancyinformation
			, @Qualifier("jLabelVdeUpperoccupancyinformation") JLabel jLabelVdeUpperoccupancyinformation
			, @Qualifier("jLabelVdeLoweroccupancyinformation") JLabel jLabelVdeLoweroccupancyinformation
			
			, @Qualifier("currentFrameJTableNameUpper") JTable currentFrameJTableNameUpper
			, @Qualifier("currentFrameJTableNameLower") JTable currentFrameJTableNameLower
			) {
		//
		this.jProgressBarAisChannelA = jProgressBarAisChannelA;
		this.jProgressBarAisChannelB = jProgressBarAisChannelB;
		this.jProgressBarAsmChannelA = jProgressBarAsmChannelA;
		this.jProgressBarAsmChannelB = jProgressBarAsmChannelB;
		this.jProgressBarVdeUpper = jProgressBarVdeUpper;
		this.jProgressBarVdeLower = jProgressBarVdeLower;
		
		this.jLabelAisChannelAoccupancyinformation = jLabelAisChannelAoccupancyinformation;
		this.jLabelAisChannelBoccupancyinformation = jLabelAisChannelBoccupancyinformation;
		this.jLabelAsmChannelAoccupancyinformation = jLabelAsmChannelAoccupancyinformation;
		this.jLabelAsmChannelBoccupancyinformation = jLabelAsmChannelBoccupancyinformation;
		this.jLabelVdeUpperoccupancyinformation = jLabelVdeUpperoccupancyinformation;
		this.jLabelVdeLoweroccupancyinformation = jLabelVdeLoweroccupancyinformation;
		
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
	}
	
	@Scheduled(cron = "0/1 * * * * *", zone = "Asia/Seoul") 
	public void process() {
		//
		CustomTableCellRenderer rendererUpper = (CustomTableCellRenderer) this.currentFrameJTableNameUpper.getDefaultRenderer(Object.class);
		Map<String, Integer> rtnValueUpper = rendererUpper.getOccupancyinformation();
		
		CustomTableCellRenderer rendererLower = (CustomTableCellRenderer) this.currentFrameJTableNameLower.getDefaultRenderer(Object.class);
		Map<String, Integer> rtnValueLower = rendererLower.getOccupancyinformation();
		
		int aisA = (int) (((double) rtnValueUpper.get("aisA") / 2250) * 100.0);
		int aisB = (int) (((double) rtnValueUpper.get("aisB") / 2250) * 100.0);
		int asmA = (int) (((double) rtnValueUpper.get("asmA") / 2250) * 100.0);
		int asmB = (int) (((double) rtnValueUpper.get("asmB") / 2250) * 100.0);
		int vdeUpper = (int) (((double) rtnValueUpper.get("vdeUpper") / 2250) * 100.0);
		int vdeLower = (int) (((double) rtnValueLower.get("vdeLower") / 2250) * 100.0);
		
		this.jProgressBarAisChannelA.setValue(aisA);
		this.jProgressBarAisChannelB.setValue(aisB);
		this.jProgressBarAsmChannelA.setValue(asmA);
		this.jProgressBarAsmChannelB.setValue(asmB);
		this.jProgressBarVdeUpper.setValue(vdeUpper);
		this.jProgressBarVdeLower.setValue(vdeLower);
		
		this.jLabelAisChannelAoccupancyinformation.setText(aisA+" %");
		this.jLabelAisChannelBoccupancyinformation.setText(aisB+" %");
		this.jLabelAsmChannelAoccupancyinformation.setText(asmA+" %");
		this.jLabelAsmChannelBoccupancyinformation.setText(asmB+" %");
		this.jLabelVdeUpperoccupancyinformation.setText(vdeUpper+" %");
		this.jLabelVdeLoweroccupancyinformation.setText(vdeLower+" %");

	}
	
}
