package com.thread.ch10_list10_4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private static final int TASKS = 10;

	public static void main(String[] args) {
		System.out.println("Begin");
		ExecutorService service = Executors.newFixedThreadPool(5);
		CountDownLatch doneLatch = new CountDownLatch(TASKS);

		try {
			for (int i = 0; i < TASKS; i++) {
				service.execute(new MyTask(doneLatch, i));
			}
			System.out.println("await");

			doneLatch.await();
			System.out.println("await end");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			service.shutdown();
			System.out.println("End");
		}
	}

}
