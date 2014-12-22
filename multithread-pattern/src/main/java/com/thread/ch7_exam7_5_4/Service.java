package com.thread.ch7_exam7_5_4;

public class Service {
	private static Thread worker = null;

	public static synchronized void service() {
		if (null != worker && worker.isAlive()) {
			worker.interrupt();
			try {
				worker.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			worker = null;
		}
		System.out.println("service");
		worker = new Thread() {
			public void run() {
				doService();
			};
		};
		worker.start();
	}

	public static void doService() {
		for (int i = 0; i < 50; i++) {
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
