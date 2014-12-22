package com.thread.ch7_exam7_4;

public class HelperThread implements Runnable {
	private final Helper helper;
	private final int count;
	private final char c;

	public HelperThread(Helper helper, int count, char c) {
		this.helper = helper;
		this.count = count;
		this.c = c;
	}

	public void run() {
		helper.handle(count, c);
	}

}
