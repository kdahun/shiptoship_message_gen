package com.all4land.generator.ais;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.all4land.generator.system.schedule.SixbitEntityGenerator;
import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;
import com.all4land.generator.util.CRC16CCITT;

@Service
public class ASMMessageUtil {
	//
	private final SixbitEntityGenerator sixbitEntityGenerator;
	private String delimiter = "!";
	private String talker = "AB";
	private String formatter = "ADM";
	
	public ASMMessageUtil(SixbitEntityGenerator sixbitEntityGenerator) {
		// TODO Auto-generated constructor stub
		this.sixbitEntityGenerator = sixbitEntityGenerator;
	}
	
	
	public List<String> getMessage(String binaryData, MmsiEntity mmsiEntity) {
		//
		List<String> returnMessageList = new ArrayList<>();
		List<String> sixBitDataList = this.sixbitEntityGenerator.makeSixbitMessage(mmsiEntity.getMmsi(), binaryData, mmsiEntity.getAsmEntity().getSlotCount());
		
		for(int i = 0; i < sixBitDataList.size() ; i++) {
			//
			StringBuilder sbCrc = new StringBuilder();
			sbCrc.append(talker);
			sbCrc.append(formatter).append(",");
			sbCrc.append(sixBitDataList.size()).append(",");
			sbCrc.append(i+1).append(",");
			sbCrc.append(mmsiEntity.getAsmMessageSequence()).append(",");
			sbCrc.append(mmsiEntity.getAsmEntity().getChannel()).append(",");
			sbCrc.append(sixBitDataList.get(i));
			
			String crc = CRC16CCITT.calculateCRC16(sbCrc.toString());
			
			StringBuilder sb = new StringBuilder();
			sb.append(delimiter);
			sb.append(talker);
			sb.append(formatter).append(",");
			sb.append(sixBitDataList.size()).append(",");
			sb.append(i+1).append(",");
			sb.append(mmsiEntity.getAsmMessageSequence()).append(",");
			sb.append(mmsiEntity.getAsmEntity().getChannel()).append(",");
			sb.append(sixBitDataList.get(i)).append(",0*").append(crc);
			
			returnMessageList.add(sb.toString());
		}
		
		return returnMessageList;
	}
	
}
