package com.thread.ch10_list10_6;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	private static final int THREADS = 2;
	/**
	 * 배리어 액션을 수행하기 위해서는 모든 스레드가 장벽에 도착해야하므로 태스크 개수는 Task/thread == 0
	 * 이어야 한다.
	 */
	private static final int TASK = 4;
	
	public static void main(String[] args) {
		System.out.println("Main Begin");

		ExecutorService service = Executors.newFixedThreadPool(THREADS);

		// 총 호출되는 barrier run의 횟수는 task/thread*phase 수
		Runnable barrierAction = new Runnable() {
			public void run() {
				System.out.println("Barrier Action");
			}
		};

		CyclicBarrier phaseBarrier = new CyclicBarrier(THREADS, barrierAction);

		CountDownLatch doneLatch = new CountDownLatch(TASK);

		try {
			for (int i = 0; i < TASK; i++) {
				service.execute(new MyTask(phaseBarrier, doneLatch, i));
			}

			System.out.println("Main Await");
			doneLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			service.shutdown();
			System.out.println("Main End");
		}
	}
}
