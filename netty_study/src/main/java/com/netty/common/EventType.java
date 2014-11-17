package com.netty.common;

public enum EventType {
	HEARTBEAT(1);

	private final int t;

	private EventType(int t) {
		this.t = t;
	}
	public int getType() {
		return t;
	}
}
