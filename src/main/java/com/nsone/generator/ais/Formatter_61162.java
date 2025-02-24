package com.nsone.generator.ais;

import com.nsone.generator.system.constant.SystemConstMessage;

import lombok.Data;

@Data
public class Formatter_61162 {
	//
//	private String datagramHeader = "UdPbC\0";
	private String datagramHeader = "UdPbC ";
	private String blockSeperator = "\\";
	private TagBlock tagBlock;
//	private TerrestrialSlotResourceRequest terrestrialSlotResourceRequest; 
	
	public Formatter_61162(TagBlock tagBlock){
		//
		this.tagBlock = tagBlock;
//		this.terrestrialSlotResourceRequest = terrestrialSlotResourceRequest;
	}
	
	public String getMessage() {
		//
		StringBuilder sb = new StringBuilder();
		sb.append(this.datagramHeader).append(this.blockSeperator);
		sb.append(this.tagBlock.getTagBlock()).append(this.blockSeperator);
		//sb.append(this.terrestrialSlotResourceRequest.getTerrestrialSlotResourceRequest()).append(SystemConstMessage.CRLF);
		return sb.toString();
	}
}
