package com.thread.ch06_exam6_3;

public class WriterThread implements Runnable {
	private final Data data;
	private final String filter;
	private int index = 0;

	public WriterThread(Data data, String filter) {
		this.data = data;
		this.filter = filter;
	}

	public void run() {
		while (true) {
			char c = nextChar();
			data.write(c);
		}
	}

	private char nextChar() {
		char c = filter.charAt(index);
		index++;
		if (index >= filter.length()) {
			index = 0;
		}
		return c;
	}
}
