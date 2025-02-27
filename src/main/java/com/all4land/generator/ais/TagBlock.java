package com.all4land.generator.ais;

import com.all4land.generator.util.CRC16CCITT;

import lombok.Data;

@Data
public class TagBlock {
	//
	private GroupControl groupControl;
	private SourceIdentification sourceIdentification;
	private DestinationIdentification destinationIdentification;
	
	public TagBlock(GroupControl groupControl, SourceIdentification sourceIdentification
			, DestinationIdentification destinationIdentification){
		//
		this.groupControl = groupControl;
		this.sourceIdentification = sourceIdentification;
		this.destinationIdentification = destinationIdentification;
	}
	
	public String getTagBlock() {
		//
		StringBuilder sbCrc = new StringBuilder();
		sbCrc.append(this.groupControl.getGroupControl());
		sbCrc.append(",");
		sbCrc.append(this.sourceIdentification.getSourceIdentification());
		sbCrc.append(",");
		sbCrc.append(this.destinationIdentification.getDestinationIdentification());
		
		String tabBlockCrc = CRC16CCITT.calculateCRC16(sbCrc.toString());
		
		StringBuilder sb = new StringBuilder();
		sb.append(sbCrc).append("*").append(tabBlockCrc);
		return sb.toString();
	}
}
