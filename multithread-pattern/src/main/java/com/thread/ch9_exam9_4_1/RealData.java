package com.thread.ch9_exam9_4_1;

public class RealData implements Data {
	private final String content;

	public RealData(int count, char c) {
		System.out.println("making realData(" + count + ", " + c + ") begin");
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			buffer[i] = c;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("making realData(" + count + ", " + c + ") end");
		this.content = new String(buffer);
	}

	public String getContent() {
		return content;
	}
}
