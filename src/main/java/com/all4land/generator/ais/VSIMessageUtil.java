package com.all4land.generator.ais;

import com.all4land.generator.util.CRC16CCITT;

import lombok.Data;

@Data
public class VSIMessageUtil {
	//
	private String delimiter = "$";
	private String talker = "AB";
	private String formatter = "VSI";
//	private String uniqueIdentifierHeader = "00";
	private String uniqueIdentifier = "";
//	private String x = "1";
	private String hhmmss_ss ;
	private String x_x0;
	private String x_x1;
	private String x_x2;
	private String astar = "*";
	private String crc;
	
	public String getMessage(String uniqueIdentifier, String hhmmss_ss, String x_x0, String x_x1, String x_x2, String sequence) {
		//
		StringBuilder sbCrc = new StringBuilder();
		sbCrc.append(talker);
		sbCrc.append(formatter).append(",");
		sbCrc.append(uniqueIdentifier).append(",");
		sbCrc.append(sequence).append(",");
		sbCrc.append(hhmmss_ss).append(",");
		sbCrc.append(x_x0).append(",");
		sbCrc.append(x_x1).append(",");
		sbCrc.append(x_x2);
		
		crc = CRC16CCITT.calculateCRC16(sbCrc.toString());
		
		StringBuilder sb = new StringBuilder();
		sb.append(delimiter);
		sb.append(talker);
		sb.append(formatter).append(",");
		sb.append(uniqueIdentifier).append(",");
		sb.append(sequence).append(",");
		sb.append(hhmmss_ss).append(",");
		sb.append(x_x0).append(",");
		sb.append(x_x1).append(",");
		sb.append(x_x2);
		sb.append(astar);
		sb.append(crc);
		return sb.toString();
	}
	
	
	
}
