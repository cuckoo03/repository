package com.thread.ch7_exam7_5_2;

public class Service {

	public static void service() {
		new Thread(new Runnable() {
			public void run() {
				doService();
			}
		}).start();
	}

	public static synchronized void doService() {
		System.out.println("service");
		for (int i = 0; i < 5; i++) {
			System.out.println(".");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("done.");
	}
}
