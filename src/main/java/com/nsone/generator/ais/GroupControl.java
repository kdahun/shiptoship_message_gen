package com.nsone.generator.ais;

import lombok.Data;

@Data
public class GroupControl {
	//
	private String header = "g";
	private String num0;
	private String num1;
	private String seq;
	private String seperator = "-";
	
	public GroupControl(int num0, int num1, int seq){
		//
		if(num0 <= 0) {
			//
			this.num0 = "1";
		}else {
			//
			this.num0 = String.valueOf(num0);
		}
		
		if(num1 <= 0) {
			//
			this.num1 = "1";
		}else {
			//
			this.num1 = String.valueOf(num1);
		}
		
		this.seq = String.valueOf(seq);
	}
	
	public String getGroupControl() {
		//
		StringBuilder sb = new StringBuilder();
		sb.append(this.header).append(":").append(this.num0).append(this.seperator).append(this.num1).append(this.seperator).append(this.seq);
		return sb.toString();
	}
}
