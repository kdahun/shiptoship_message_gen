package com.nsone.generator.ais;

import java.time.LocalDateTime;

import com.nsone.generator.ui.util.TimeString;
import com.nsone.generator.util.CRC16CCITT;

import lombok.Data;

@Data
public class ESIMessageUtil {
	//
	private String delimiter = "$";
	private String talker = "VA";
	private String formatter = "ESI";
	private String seperator = ",";
	
	private String shoreStationId = "";
	private String channelLeg = "";
	private String linkId = "19";
	private String hhmmssSS;
	private String tdmaFrame; // 0 to 24
	private String tdmachannel; // 0 to 5
	private String totalAccountSlot; // 1 to 14
	private String firstSlotNumber; // 0 to 2249
	
	public ESIMessageUtil(String shoreStationId, String channelLeg, String tdmaFrame, String tdmachannel
			, String totalAccountSlot, String firstSlotNumber, String linkId){
		//
		this.shoreStationId = shoreStationId;
		this.channelLeg = channelLeg;
		this.tdmaFrame = tdmaFrame;
		this.tdmachannel = tdmachannel;
		this.totalAccountSlot = totalAccountSlot;
		this.firstSlotNumber = firstSlotNumber;
		this.linkId = linkId;
		
		LocalDateTime now = LocalDateTime.now();
		
		this.hhmmssSS = TimeString.getESIFormat(now);
	}
	
	public String getMessage() {
		//
		StringBuilder sbCrc = new StringBuilder();
		sbCrc.append(this.talker);
		sbCrc.append(this.formatter);
		sbCrc.append(this.seperator);
		sbCrc.append(this.shoreStationId);
		sbCrc.append(this.seperator);
		sbCrc.append(this.channelLeg);
		sbCrc.append(this.seperator);
		sbCrc.append(this.linkId);
		sbCrc.append(this.seperator);
		
		sbCrc.append(this.hhmmssSS);
		sbCrc.append(this.seperator);
		sbCrc.append(this.tdmaFrame);
		sbCrc.append(this.seperator);
		sbCrc.append(this.tdmachannel);
		sbCrc.append(this.seperator);
		sbCrc.append(this.totalAccountSlot);
		sbCrc.append(this.seperator);
		sbCrc.append(this.firstSlotNumber);
				
		String crc = CRC16CCITT.calculateCRC16(sbCrc.toString());
		
		StringBuilder sb = new StringBuilder();
		sb.append(this.delimiter);
		sb.append(sbCrc);
		sb.append("*");
		sb.append(crc);
		
		return sb.toString();
	}
}
