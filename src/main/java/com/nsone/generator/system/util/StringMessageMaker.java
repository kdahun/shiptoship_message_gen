package com.nsone.generator.system.util;

import java.time.LocalDateTime;

import com.nsone.generator.system.constant.SystemConstMessage;
import com.nsone.generator.ui.tab.ais.entity.AisCellInfos;
import com.nsone.generator.ui.tab.ais.entity.AsmCellInfos;
import com.nsone.generator.ui.tab.ais.entity.CellInfos;
import com.nsone.generator.ui.tab.ais.entity.MmsiEntity;
import com.nsone.generator.ui.tab.ais.entity.VdeLowerLegCellInfos;
import com.nsone.generator.ui.tab.ais.entity.VdeUpperLegCellInfos;

public class StringMessageMaker {
	// ----------------------------
	// Slot Number : 
	// ----------------------------
	// MMSI :
	// Channel :
	// Time :
	//
	// Channel :
	// Time :
	// ----------------------------
	// MMSI :
	// Channel :
	// Time :
	//
	// Channel :
	// Time :
	
	public static String tooltipMessage(CellInfos cellInfos) {
		//
		StringBuilder sbTime = new StringBuilder();
		sbTime.append(makeHeader(cellInfos.getSlotNumber()));
		
		if(cellInfos.getAisCellInfos() != null) {
			//
			sbTime.append(makeAisBody(cellInfos));
		}
		if(cellInfos.getAsmCellInfosForA() != null || cellInfos.getAsmCellInfosForB() != null) {
			//
			sbTime.append(makeAsmBody(cellInfos.getAsmCellInfosForA(), cellInfos.getAsmCellInfosForB()));
		}
		if(cellInfos.getVdeUpperLegCellInfos() != null || cellInfos.getVdeLowerLegCellInfos() != null) {
			//
			sbTime.append(makeVdeBody(cellInfos.getVdeUpperLegCellInfos(), cellInfos.getVdeLowerLegCellInfos()));
		}
		
		return sbTime.toString();
	}
	
	// ----------------------------
	// Slot Number : 
	// ----------------------------
	private static StringBuilder makeHeader(int slotNumber) {
		//
		StringBuilder sbTime = new StringBuilder();
		sbTime.append("----------------------------").append(SystemConstMessage.CRLF);
		sbTime.append("SlotNumber : ").append(slotNumber).append(SystemConstMessage.CRLF);
		sbTime.append("----------------------------");
		return sbTime;
	}
	
	// MMSI :
	// slot time out
	// Channel :
	// Time :
	//
	// Channel :
	// Time :
	// ----------------------------
	private static StringBuilder makeAisBody(CellInfos cellInfos) {
		//
		StringBuilder sbTime = new StringBuilder().append(SystemConstMessage.CRLF);
		sbTime.append("AIS MMSI : ").append(cellInfos.getAisCellInfos().getMmsi()).append(SystemConstMessage.CRLF);
		
		if(cellInfos.getAisCellInfos().getMmsiEntity().getSpeed() == 180) {
			sbTime.append("keep flag : true").append(SystemConstMessage.CRLF);
		}else {
			sbTime.append("slot time out : ").append(cellInfos.getAisCellInfos().getSlotTimeOut()).append(SystemConstMessage.CRLF);
		}
		
		if(cellInfos.getAisCellInfos().getChannelATime() != null) {
			//
			sbTime.append("Channel A : ").append("A").append(SystemConstMessage.CRLF);
			sbTime.append("time : ").append(cellInfos.getAisCellInfos().getChannelATime().format(SystemConstMessage.formatterForTooltip));
			if(cellInfos.getAisCellInfos().getChannelBTime() != null) {
				sbTime.append(SystemConstMessage.CRLF).append(SystemConstMessage.CRLF);
			}
		}
		if(cellInfos.getAisCellInfos().getChannelBTime() != null) {
			//
			sbTime.append("Channel B : ").append("B").append(SystemConstMessage.CRLF);
			sbTime.append("time : ").append(cellInfos.getAisCellInfos().getChannelBTime().format(SystemConstMessage.formatterForTooltip));
		}
		sbTime.append(SystemConstMessage.CRLF).append("----------------------------").append(SystemConstMessage.CRLF);
		return sbTime;
	}
	
	// ASM MMSI :
	// Channel :
	// Time :
	//
	// ASM MMSI :
	// Channel :
	// Time :
	// ----------------------------
	private static StringBuilder makeAsmBody(AsmCellInfos asmCellInfosForA, AsmCellInfos asmCellInfosForB) {
		//
		StringBuilder sbTime = new StringBuilder().append(SystemConstMessage.CRLF);
		
		if(asmCellInfosForA != null) {
			sbTime.append("ASM MMSI : ").append(asmCellInfosForA.getMmsi()).append(SystemConstMessage.CRLF);
			sbTime.append("Channel A : ").append("A").append(SystemConstMessage.CRLF);
			sbTime.append("time : ").append(asmCellInfosForA.getTime().format(SystemConstMessage.formatterForTooltip));
			
			if(asmCellInfosForB != null) {
				sbTime.append(SystemConstMessage.CRLF).append(SystemConstMessage.CRLF);
			}
		}
		
		if(asmCellInfosForB != null) {
			sbTime.append("ASM MMSI : ").append(asmCellInfosForB.getMmsi()).append(SystemConstMessage.CRLF);
			sbTime.append("Channel B : ").append("B").append(SystemConstMessage.CRLF);
			sbTime.append("time : ").append(asmCellInfosForB.getTime().format(SystemConstMessage.formatterForTooltip));
			
		}
		
		sbTime.append(SystemConstMessage.CRLF).append("----------------------------");
		return sbTime;
	}
	
	private static StringBuilder makeVdeBody(VdeUpperLegCellInfos vdeUpperLegCellInfos, VdeLowerLegCellInfos vdeLowerLegCellInfos) {
		//
		StringBuilder sbTime = new StringBuilder().append(SystemConstMessage.CRLF);
		
		if(vdeUpperLegCellInfos != null) {
			sbTime.append("VDE MMSI : ").append(vdeUpperLegCellInfos.getMmsi()).append(SystemConstMessage.CRLF);
			sbTime.append("time : ").append(vdeUpperLegCellInfos.getChannelTime().format(SystemConstMessage.formatterForTooltip));
			
			if(vdeLowerLegCellInfos != null) {
				sbTime.append(SystemConstMessage.CRLF).append(SystemConstMessage.CRLF);
			}
		}
		
		if(vdeLowerLegCellInfos != null) {
			sbTime.append("VDE MMSI : ").append(vdeLowerLegCellInfos.getMmsi()).append(SystemConstMessage.CRLF);
			sbTime.append("time : ").append(vdeLowerLegCellInfos.getChannelTime().format(SystemConstMessage.formatterForTooltip));
			
		}
		
		sbTime.append(SystemConstMessage.CRLF).append("----------------------------");
		return sbTime;
	}
	
	public static String tooltipMessage(MmsiEntity mmsiEntity, char channel, int index, LocalDateTime time) {
		//
		String timeString = time.format(SystemConstMessage.formatterForTooltip);
		StringBuilder sbTime = new StringBuilder();
		sbTime.append("MMSI : ").append(mmsiEntity.getMmsi()).append(SystemConstMessage.CRLF);
		sbTime.append("Channel : ").append('A' == channel ? "A":"B").append(SystemConstMessage.CRLF);
		sbTime.append("SlotNumber : ").append(index).append(SystemConstMessage.CRLF);
		sbTime.append("time : ").append(timeString);
		return sbTime.toString();
	}
	
	
	
	public static String tooltipDefault(String index) {
		//
		StringBuilder sbTime = new StringBuilder();
		sbTime.append("SlotNumber : ").append(index).append(SystemConstMessage.CRLF);
		return sbTime.toString();
	}
}
