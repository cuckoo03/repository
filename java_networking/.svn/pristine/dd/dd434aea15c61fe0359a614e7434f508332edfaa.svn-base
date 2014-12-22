package com.thread.ch06_exam6_2;

public class Data {
	private final char[] buffer;

	public Data(int size) {
		this.buffer = new char[size];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = '*';
		}
	}

	public char[] read() {
		return doRead();
	}

	public void write(char c) {
		doWrite(c);
	}

	private void doWrite(char c) {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = c;
			slowly();
		}
	}

	private void slowly() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private char[] doRead() {
		char[] newBuffer = new char[buffer.length];
		for (int i = 0; i < buffer.length; i++) {
			newBuffer[i] = buffer[i];
		}
		slowly();
		return newBuffer;
	}
}