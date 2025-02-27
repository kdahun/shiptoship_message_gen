package com.all4land.generator;

import io.netty.util.CharsetUtil;

public class DatagramHeader {
	//
	private String token = "RaUdP "; //"RaUdp\0"; // 길이 6
	private int headerVersion = 2; // 길이 2
	private int headerLength = 38; // 길이 2 총길이 38
	private String srcId = "DC0100"; // 길이 6
	private String destId = "SG0100"; // 길이 6
	private int type = 21; // 길이 2
	private int blockId = 0; // 길이 4
	private int seqNumber = 0; // 길이 4
	private int maxSeq = 0; // 길이 4
	private int device = 1; // 길이 1
	private int channel = 1; // 길이 1
	
	
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getHeaderVersion() {
		return headerVersion;
	}

	public void setHeaderVersion(int headerVersion) {
		this.headerVersion = headerVersion;
	}

	public int getHeaderLength() {
		return headerLength;
	}

	public void setHeaderLength(int headerLength) {
		this.headerLength = headerLength;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	public int getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	public int getMaxSeq() {
		return maxSeq;
	}

	public void setMaxSeq(int maxSeq) {
		this.maxSeq = maxSeq;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public byte[] getTokenBytes() {
		//
		return this.token.getBytes(CharsetUtil.UTF_8);
	}
	
	public byte[] getHeaderVersionBytes() {
		//
		byte[] b = new byte[2];
		// uint16 변환
        b[0] = (byte) ((this.headerVersion >> 8) & 0xFF);
        b[1] = (byte) (this.headerVersion & 0xFF);
        
        return b;
	}
	
	public byte[] getHeaderLengthBytes() {
		//
		byte[] b = new byte[2];
        b[0] = (byte) ((this.headerLength >> 8) & 0xFF);
        b[1] = (byte) (this.headerLength & 0xFF);
        
        return b;
	}
	
	public byte[] getSrcIdBytes() {
		//
        return this.srcId.getBytes(CharsetUtil.UTF_8);
	}
	
	public byte[] getDestIdBytes() {
		//
		return this.destId.getBytes(CharsetUtil.UTF_8);
	}
	
	public byte[] getTypeBytes() {
		//
		byte[] b = new byte[2];
        b[0] = (byte) ((this.type >> 8) & 0xFF);
        b[1] = (byte) (this.type & 0xFF);
        return b;
	}
	
	public byte[] getBlockIdBytes() {
		//
		byte[] b = new byte[4];
		b[0] = (byte) ((this.blockId >> 24) & 0xFF);
        b[1] = (byte) ((this.blockId >> 16) & 0xFF);
        b[2] = (byte) ((this.blockId >> 8) & 0xFF);
        b[3] = (byte) (this.blockId & 0xFF);
        
        return b;
	}
	
	public byte[] getSeqNumberBytes() {
		//
		byte[] b = new byte[4];
		b[0] = (byte) ((this.seqNumber >> 24) & 0xFF);
        b[1] = (byte) ((this.seqNumber >> 16) & 0xFF);
        b[2] = (byte) ((this.seqNumber >> 8) & 0xFF);
        b[3] = (byte) (this.seqNumber & 0xFF);
        
        return b;
	}
	
	public byte[] getMaxSeqBytes() {
		//
		byte[] b = new byte[4];
		b[0] = (byte) ((this.maxSeq >> 24) & 0xFF);
        b[1] = (byte) ((this.maxSeq >> 16) & 0xFF);
        b[2] = (byte) ((this.maxSeq >> 8) & 0xFF);
        b[3] = (byte) (this.maxSeq & 0xFF);
        
        return b;
	}
	
	public byte[] getDeviceBytes() {
		//
		byte[] b = new byte[1];
        b[0] = (byte) (this.device & 0xFF);
        
        return b;
	}
	
	public byte[] getChannelBytes() {
		//
		byte[] b = new byte[1];
        b[0] = (byte) (this.channel & 0xFF);
        
        return b;
	}
	
	public byte[] getDatagramHeaderBytes() {
		//
		// 전체 데이터 길이 계산: 6 + 2 + 2 + 6 + 6 + 2 + 4 + 4 + 4 + 1 + 1 = 38
		byte[] b = new byte[38];
		int pos = 0;
        System.arraycopy(getTokenBytes(), 0, b, pos, 6);
        pos += 6;
        System.arraycopy(getHeaderVersionBytes(), 0, b, pos, 2);
        pos += 2;
        System.arraycopy(getHeaderLengthBytes(), 0, b, pos, 2);
        pos += 2;
        System.arraycopy(getSrcIdBytes(), 0, b, pos, 6);
        pos += 6;
        System.arraycopy(getDestIdBytes(), 0, b, pos, 6);
        pos += 6;
        System.arraycopy(getTypeBytes(), 0, b, pos, 2);
        pos += 2;
        System.arraycopy(getBlockIdBytes(), 0, b, pos, 4);
        pos += 4;
        System.arraycopy(getSeqNumberBytes(), 0, b, pos, 4);
        pos += 4;
        System.arraycopy(getMaxSeqBytes(), 0, b, pos, 4);
        pos += 4;
        System.arraycopy(getDeviceBytes(), 0, b, pos, 1);
        pos += 1;
        System.arraycopy(getChannelBytes(), 0, b, pos, 1);
        
		return b;
	}
}
