package com.thread.ch06_list6_1;

public class WriterThread implements Runnable {
	private final Data data;
	private final String filter;
	private int index = 0;

	public WriterThread(Data data, String filter) {
		this.data = data;
		this.filter = filter;
	}

	public void run() {
		try {
			while (true) {
				char c = nextChar();
				data.write(c);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
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
