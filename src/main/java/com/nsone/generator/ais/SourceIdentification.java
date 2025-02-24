package com.nsone.generator.ais;

import lombok.Data;

@Data
public class SourceIdentification {
	//
	private String header = "s";
	private String value;
	
	public SourceIdentification(String sourceIdentification){
		//
		if(sourceIdentification == null || "".equals(sourceIdentification)) {
			//
			this.value = "MV0001";
		}else {
			this.value = sourceIdentification;
		}
	}
	
	public String getSourceIdentification() {
		//
		StringBuilder sb = new StringBuilder();
		sb.append(this.header).append(":").append(this.value);
		return sb.toString();
	}
}
