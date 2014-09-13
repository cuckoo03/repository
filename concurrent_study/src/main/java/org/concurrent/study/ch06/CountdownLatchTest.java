package org.concurrent.study.ch06;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountdownLatchTest {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(4);
		CountDownLatch latch = new CountDownLatch(10);

		try {
			System.out.println("start");
			for (int i = 0; i < 10; i++) {
				executor.execute(new MyThread(latch, i));
			}
			System.out.println("await");
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
			System.out.println("end");
		}
	}
}

class MyThread extends Thread {
	private int count;
	private CountDownLatch latch;

	public MyThread(CountDownLatch latch, int i) {
		this.latch = latch;
		this.count = i;
	}

	@Override
	public void run() {
		System.out.println(count);
		latch.countDown();
	}
}