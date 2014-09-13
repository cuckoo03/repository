package com.netty.log.vo;

import java.sql.Timestamp;

public class MyPacket {
	private long clientId;
	private int receiveDataLength;
	private String receiveData;
	private Timestamp receiveTime;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public int getReceiveDataLength() {
		return receiveDataLength;
	}

	public void setReceiveDataLength(int receiveDataLength) {
		this.receiveDataLength = receiveDataLength;
	}

	public String getReceiveData() {
		return receiveData;
	}

	public void setReceiveData(String receiveData) {
		this.receiveData = receiveData;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Override
	public String toString() {
		return "clientID:" + this.clientId + " receive Data:"
				+ this.receiveData + " receive Time:" + this.receiveTime;
	}
}
