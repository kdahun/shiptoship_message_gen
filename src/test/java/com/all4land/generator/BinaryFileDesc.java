package com.all4land.generator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.netty.util.CharsetUtil;

public class BinaryFileDesc {
	//
	private int descLength = 0; // 길이 4
	private int fileLength = 0; // 길이 4 파일 바이트의 전체길이
	private int statusOfAcquisition = 0; // 길이 2
	private int ackDestPort = 0; // 길이 2
	private int typeLength = 0; // 길이 1
	private String dataType; // 길이 20
	private int statusLength = 0; // 길이 2
	private String statusAndInfoText = "success"; // 길이 n
	
	private byte[] binaryFileData;
	private String binaryFileDataString;
	
	public String getBinaryFileDataString() {
		//
		return this.binaryFileDataString;
	}
	
	public int getTypeLength() {
		return typeLength;
	}

	public void setTypeLength(int typeLength) {
		this.typeLength = typeLength;
	}

	public int getStatusLength() {
		return statusLength;
	}

	public void setStatusLength(int statusLength) {
		this.statusLength = statusLength;
	}

	public byte[] getBinaryFileData() {
		return binaryFileData;
	}

	public void setBinaryFileData(byte[] binaryFileData) {
		this.binaryFileData = binaryFileData;
		this.binaryFileDataString = new String(binaryFileData, CharsetUtil.UTF_8);
	}

	public int getDescLength() {
		return descLength;
	}

	public int getFileLength() {
		return fileLength;
	}

	public int getStatusOfAcquisition() {
		return statusOfAcquisition;
	}

	public int getAckDestPort() {
		return ackDestPort;
	}

	public String getDataType() {
		return dataType;
	}

	public String getStatusAndInfoText() {
		return statusAndInfoText;
	}

	public void setDescLength(int value) {
		//
		this.descLength = value;
	}
	
	public void setFileLength(int value) {
		//
		this.fileLength = value;
	}
	
	public void setStatusOfAcquisition(int value) {
		//
		this.statusOfAcquisition = value;
	}
	
	public void setAckDestPort(int value) {
		//
		this.ackDestPort = value;
	}
	
	public void setStatusAndInfoText(String value) {
		//
		this.statusAndInfoText = value;
		this.statusLength = this.statusAndInfoText.length(); 
	}
	
	public void setDataType(String value) {
		//
		this.dataType = value;
		this.typeLength = this.dataType.length(); 
	}
	
	public byte[] getDescLengthBytes() {
		//
		byte[] b = new byte[4];
		b[0] = (byte) ((this.descLength >> 24) & 0xFF);
        b[1] = (byte) ((this.descLength >> 16) & 0xFF);
        b[2] = (byte) ((this.descLength >> 8) & 0xFF);
        b[3] = (byte) (this.descLength & 0xFF);
        
     // ByteBuffer를 사용하여 byte 배열을 int로 변환
//        ByteBuffer buffer = ByteBuffer.wrap(b);
//        buffer.order(ByteOrder.BIG_ENDIAN); // 빅엔디언으로 설정
//
//        int value = buffer.getInt(); // 4바이트를 int로 변환
//        System.out.println("값: " + value); // 출력: 49
        
        
        return b;
	}
	
	public byte[] getFileLengthBytes() {
		//
		byte[] b = new byte[4];
		b[0] = (byte) ((this.fileLength >> 24) & 0xFF);
        b[1] = (byte) ((this.fileLength >> 16) & 0xFF);
        b[2] = (byte) ((this.fileLength >> 8) & 0xFF);
        b[3] = (byte) (this.fileLength & 0xFF);
        return b;
	}
	
	public byte[] getStatusOfAcquisitionBytes() {
		//
		byte[] b = new byte[2];
        b[0] = (byte) ((this.statusOfAcquisition >> 8) & 0xFF);
        b[1] = (byte) (this.statusOfAcquisition & 0xFF);
        return b;
	}
	
	public byte[] getAckDestPortBytes() {
		//
		byte[] b = new byte[2];
        b[0] = (byte) ((this.ackDestPort >> 8) & 0xFF);
        b[1] = (byte) (this.ackDestPort & 0xFF);
        
        ByteBuffer buffer = ByteBuffer.wrap(b);
        buffer.order(ByteOrder.BIG_ENDIAN); // 빅엔디언으로 설정

        short value = buffer.getShort(); // 4바이트를 int로 변환
        System.out.println("값: " + value); // 출력: 49
        
        
        return b;
	}
	
	public byte[] getTypeLengthBytes() {
		//
		byte[] b = new byte[1];
        b[0] = (byte) (this.typeLength & 0xFF);
        return b;
	}
	
	public byte[] getDataTypeBytes() {
		//
		return this.dataType.getBytes(CharsetUtil.UTF_8);
//		byte[] bytes = dataType.getBytes(StandardCharsets.UTF_8);
//        if (bytes.length < 20) {
//            // Create a new byte array with 20 bytes
//            byte[] paddedBytes = new byte[20];
//            // Copy the original bytes into the new byte array
//            System.arraycopy(bytes, 0, paddedBytes, 0, bytes.length);
//            // The remaining bytes are already initialized to 0 by default
//            return paddedBytes;
//        } else {
//            // If the byte array is longer than 20 bytes, truncate it
//            return Arrays.copyOf(bytes, 20);
//        }
	}
	
	public byte[] getStatusLengthBytes() {
		//
		byte[] b = new byte[2];
        b[0] = (byte) ((this.statusLength >> 8) & 0xFF);
        b[1] = (byte) (this.statusLength & 0xFF);
        return b;
	}
	
	public byte[] getStatusAndInfoTextBytes() {
		//
		return this.statusAndInfoText.getBytes(CharsetUtil.UTF_8);
	}
	
	public byte[] getBinaryFileDescBytes() {
		//
		byte[] statusAndInfoText_bytes = getStatusAndInfoTextBytes();
		int statusAndInfoText_Length = statusAndInfoText_bytes.length;
		// 전체 데이터 길이 계산: 4 + 4+ 2 + 2 + 1  + 20 + 2 + n
		byte[] b = new byte[4 + 4 + 2 + 2 + 1 + this.getDataTypeBytes().length + 2 + statusAndInfoText_Length];
		
		int DataTypeLength = this.getDataTypeBytes().length;
		
		int descLength = b.length;
		this.setDescLength(descLength);
		
		int pos = 0;
        System.arraycopy(this.getDescLengthBytes(), 0, b, pos, this.getDescLengthBytes().length);
        pos += this.getDescLengthBytes().length;
        System.arraycopy(this.getFileLengthBytes(), 0, b, pos, this.getFileLengthBytes().length);
        pos += this.getFileLengthBytes().length;
        System.arraycopy(this.getStatusOfAcquisitionBytes(), 0, b, pos, this.getStatusOfAcquisitionBytes().length);
        pos += this.getStatusOfAcquisitionBytes().length;
        System.arraycopy(this.getAckDestPortBytes(), 0, b, pos, this.getAckDestPortBytes().length);
        pos += this.getAckDestPortBytes().length;
        System.arraycopy(this.getTypeLengthBytes(), 0, b, pos, this.getTypeLengthBytes().length);
        pos += this.getTypeLengthBytes().length;
        System.arraycopy(this.getDataTypeBytes(), 0, b, pos, this.getDataTypeBytes().length);
        pos += this.getDataTypeBytes().length;
        System.arraycopy(this.getStatusLengthBytes(), 0, b, pos, this.getStatusLengthBytes().length);
        pos += this.getStatusLengthBytes().length;
        System.arraycopy(statusAndInfoText_bytes, 0, b, pos, statusAndInfoText_bytes.length);
        
		return b;
	}
}
