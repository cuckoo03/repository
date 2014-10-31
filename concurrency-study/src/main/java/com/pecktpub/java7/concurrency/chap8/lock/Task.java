package com.pecktpub.java7.concurrency.chap8.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Task implements Runnable {
	private Lock lock;

	public Task(Lock lock) {
		this.lock = lock;
	}

	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			lock.lock();
			System.out.println("Get the lock:"
					+ Thread.currentThread().getName());

			try {
				TimeUnit.MILLISECONDS.sleep(500);
				System.out.println("Free the lock:"
						+ Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

}
