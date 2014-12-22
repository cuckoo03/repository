package com.thread.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
	private static int count = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();

		for (int i = 0; i < 100; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						while (true) {
							System.out.println(Thread.currentThread().getName()
									+ ":" + count++);
							Thread.sleep(100);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		executorService.shutdown();

		try {
			if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
				System.out.println("still working");
				executorService.shutdownNow();
				
				if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
					System.out.println("still still working");
				}
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}
		
		System.out.println("shutdown complete");
	}
}
