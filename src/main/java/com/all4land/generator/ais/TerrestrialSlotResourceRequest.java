package com.all4land.generator.ais;

import com.all4land.generator.util.CRC16CCITT;

import lombok.Data;

@Data
public class TerrestrialSlotResourceRequest {
	//
	private String delimiter = "$";
	private String talker = "VA";
	private String formatter = "TSQ";
	private String seperator = ",";
	private String seq = "";
	private String shoreStationId = "";
	private String sourceId = "";
	private String physicalChannelNumber = "1284";
	private String linkId = "19";
	
	public TerrestrialSlotResourceRequest(String seq, String shoreStationId, String sourceId, String physicalChannelNumber, String linkId){
		//
		this.seq = seq;
		this.shoreStationId = shoreStationId;
		this.sourceId = sourceId;
		this.physicalChannelNumber = physicalChannelNumber;
		this.linkId = linkId;
	}
	
	public String getTerrestrialSlotResourceRequest() {
		//
		StringBuilder sbCrc = new StringBuilder();
		sbCrc.append(this.talker);
		sbCrc.append(this.formatter);
		sbCrc.append(this.seperator);
		
		sbCrc.append("111"); // field 1
		sbCrc.append(this.seperator);
		
		sbCrc.append(this.seq); // field 2
		sbCrc.append(this.seperator);
		
		sbCrc.append(this.shoreStationId); // field 3
		sbCrc.append(this.seperator);
		
		sbCrc.append(this.sourceId); // field 4
		sbCrc.append(this.seperator);
		
		sbCrc.append(this.shoreStationId); // field 5
		sbCrc.append(this.seperator);
		
		sbCrc.append(this.sourceId); // field 6
		sbCrc.append(this.seperator);
		
		sbCrc.append(this.physicalChannelNumber); // field 7
		sbCrc.append(this.seperator);
		
		sbCrc.append(this.linkId);  // field 8
		
		String crc = CRC16CCITT.calculateCRC16(sbCrc.toString());
		
		StringBuilder sb = new StringBuilder();
		sb.append(this.delimiter);
		sb.append(sbCrc);
		sb.append("*");
		sb.append(crc);
		
		return sb.toString();
	}
}
