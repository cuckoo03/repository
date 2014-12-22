package com.thread.chap01;

public class Gate {
	private int count = 0;
	private String name = "Nobody";
	private String address = "nowhere";

	public synchronized void pass(String name, String address) {
		this.count++;
		this.name = name;
		//			Thread.sleep(1000);
		Thread.yield();
		this.address = address;
		check();
	}

	public synchronized String toString() {
		return "no." + count + ":" + name + "," + address;
	}

	private void check() {
		if (name.charAt(0) != address.charAt(0)) {
			System.out.println("------------------------broken:" + toString());
		}
	}
}
