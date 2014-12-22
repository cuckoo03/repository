package com.thread.ch06_list6_1;

public class ReaderThread implements Runnable {
	private final Data data;

	public ReaderThread(Data data) {
		this.data = data;
	}

	public void run() {
		try {
			/*
			while (true) {
				char[] readBuffer = data.read();
				System.out.println(Thread.currentThread().getName() + " read "
						+ String.valueOf(readBuffer));
			}
			*/
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 10; i++) {
				char[] readBuffer = data.read();
				System.out.println(Thread.currentThread().getName() + " read "
						+ String.valueOf(readBuffer));
			}
			long time = System.currentTimeMillis() - begin;
			System.out.println(Thread.currentThread().getName() + " : time = "
					+ time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
