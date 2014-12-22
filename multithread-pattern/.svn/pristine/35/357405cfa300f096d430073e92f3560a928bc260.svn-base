package com.thread.ch11_exam11_6_1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private static final int TASK = 10;

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();

		try {
			for (int i = 0; i < TASK; i++) {
				Runnable printTask = new Runnable() {
					public void run() {
						Log.println("Hello");
						Log.close();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};

				new Thread(printTask).start();
			}
		} finally {
			service.shutdown();
		}
	}
}
