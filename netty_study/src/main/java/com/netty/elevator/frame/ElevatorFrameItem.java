package com.netty.elevator.frame;

public class ElevatorFrameItem {
	public ElevatorFrameItem(String name, int length) {
		this.name = name;
		this.length = length;
	}

	private String name;

	public String getName() {
		return name;
	}

	private int length;

	public int getLength() {
		return length;
	}

	public void addMessage(Object value, Object message) {

	}
}
