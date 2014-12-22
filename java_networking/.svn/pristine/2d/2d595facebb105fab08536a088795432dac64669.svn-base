package com.thread.ch10_list10_6;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private static final int THREADS = 3;

	public static void main(String[] args) {
		System.out.println("Begin");

		ExecutorService service = Executors.newFixedThreadPool(THREADS);

		Runnable barrierAction = new Runnable() {
			public void run() {
				System.out.println("Barrier Action");
			}
		};

		CyclicBarrier phaseBarrier = new CyclicBarrier(THREADS, barrierAction);

		CountDownLatch doneLatch = new CountDownLatch(THREADS);

		try {
			for (int i = 0; i < THREADS; i++) {
				service.execute(new MyTask(phaseBarrier, doneLatch, i));
			}

			System.out.println("Await");
			doneLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			service.shutdown();
			System.out.println("End");
		}
	}
}
