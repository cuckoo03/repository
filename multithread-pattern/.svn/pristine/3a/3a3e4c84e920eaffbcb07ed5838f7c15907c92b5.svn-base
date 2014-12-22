package com.thread.ch10_exam10_7;

public class Main {
	public static void main(String[] args) {
		System.out.println("main begin");

		HanoiThread t = new HanoiThread();
		t.start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("main shutdownRequest");
		t.shutdownRequest();

		System.out.println("main job");
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("main end");
	}
}
