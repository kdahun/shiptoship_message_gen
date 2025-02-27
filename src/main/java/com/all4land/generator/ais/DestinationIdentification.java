package com.all4land.generator.ais;

import lombok.Data;

@Data
public class DestinationIdentification {
	//
	private String header = "d";
	private String value;
	
	public DestinationIdentification(String destinationIdentification){
		//
		if(destinationIdentification == null || "".equals(destinationIdentification)) {
			//
			this.value = "TB0001";
		}else {
			this.value = destinationIdentification;
		}
	}
	
	public String getDestinationIdentification() {
		//
		StringBuilder sb = new StringBuilder();
		sb.append(this.header).append(":").append(this.value);
		return sb.toString();
	}
}
