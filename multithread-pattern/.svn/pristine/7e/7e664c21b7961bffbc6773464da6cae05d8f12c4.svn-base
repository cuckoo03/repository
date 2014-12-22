package com.thread.ch7_exam7_5_3;

public class Service {
	private static boolean executed = false;

	public static synchronized void service() {
		if (executed) {
			System.out.println("balking");
			return;
		}
		executed = true;

		new Thread(new Runnable() {
			public void run() {
				doService();
			}
		}).start();
	}

	private static void doService() {
		try {
			System.out.print("service");
			for (int i = 0; i < 10; i++) {
				System.out.print(".");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("done.");
		} finally {
			executed = false;
		}
	}

}
