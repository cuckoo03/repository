package com.thread.ch10_list10_6;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MyTask implements Runnable {
	private static final int PHASE = 5;
	private CyclicBarrier phaseBarrier;
	private CountDownLatch doneLatch;
	private int context;

	public MyTask(CyclicBarrier phaseBarrier, CountDownLatch doneLatch, int i) {
		this.phaseBarrier = phaseBarrier;
		this.doneLatch = doneLatch;
		this.context = i;
	}

	public void run() {
		try {
			for (int phase = 0; phase < PHASE; phase++) {
				doPhase(phase);
				phaseBarrier.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		} finally {
			doneLatch.countDown();
		}
	}

	private void doPhase(int phase) {
		String name = Thread.currentThread().getName();
		System.out.println(name + ":MyTask begin:context=" + context
				+ ", phase=" + phase);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println(name + ":MyTask end:context=" + context
					+ ", phase=" + phase);
		}
	}

}
