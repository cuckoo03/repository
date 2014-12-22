package com.thread.ch10_list10_4;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MyTask implements Runnable {
	private final CountDownLatch doneLatch;
	private final int context;
	private static final Random random = new Random(11111);

	// 실제 업무를 처리
	public MyTask(CountDownLatch doneLatch, int context) {
		this.doneLatch = doneLatch;
		this.context = context;
	}

	public void run() {
		doTask();
		doneLatch.countDown();
	}

	private void doTask() {
		String name = Thread.currentThread().getName();
		System.out.println(name + ":MyTask begin:context= " + context);
		try {
			Thread.sleep(random.nextInt(3000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println(name + ":MyTask end:context= " + context);
		}
	}

}
