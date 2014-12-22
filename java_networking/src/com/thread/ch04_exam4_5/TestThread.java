package com.thread.ch04_exam4_5;

public class TestThread implements Runnable {
	public void run() {
		System.out.println("begin");
		for (int i = 0; i < 50; i++) {
			System.out.println(".");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("end");
	}
}
