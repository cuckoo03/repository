package org.concurrent.study.ch06;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {

	private static final int THREADS = 3;

	public static void main(String[] args) {
		System.out.println("begin");

		ExecutorService executor = Executors.newFixedThreadPool(3);

		Runnable barrierAction = new Runnable() {
			public void run() {
				System.out.println("barrier action");
			}
		};

		CyclicBarrier barrier = new CyclicBarrier(THREADS, barrierAction);
		CountDownLatch latch = new CountDownLatch(THREADS);

		try {
			for (int i = 0; i < THREADS; i++) {
				executor.execute(new BarrierThread(barrier, latch, i));
			}

			System.out.println("await");
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
			System.out.println("end");
		}
	}

}

class BarrierThread extends Thread {
	private static final int PHASE = 5;
	private CyclicBarrier barrier;
	private CountDownLatch latch;
	private int count;

	public BarrierThread(CyclicBarrier barrier, CountDownLatch latch, int i) {
		this.barrier = barrier;
		this.latch = latch;
		this.count = i;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < PHASE; i++) {
				System.out.println("context:" + count + " phase:" + i);
				barrier.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		} finally {
			latch.countDown();
		}
	}
}
