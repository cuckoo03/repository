package com.thread.ch06_exam6_2;

public class ReaderThread implements Runnable {
	private final Data data;

	public ReaderThread(Data data) {
		this.data = data;
	}

	public void run() {
		while (true) {
			char[] readBuffer = data.read();
			System.out.println(Thread.currentThread().getName() + " read "
					+ String.valueOf(readBuffer));
		}
	}
}
