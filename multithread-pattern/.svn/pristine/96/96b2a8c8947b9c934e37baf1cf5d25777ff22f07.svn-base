package com.thread.chap01_2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class BoundedResource {
	private Semaphore semaphore;
	private int permits;
	private final static Random random = new Random(314159);

	public BoundedResource(int permits) {
		this.semaphore = new Semaphore(permits);
		this.permits = permits;
	}

	public void use() throws InterruptedException {
		semaphore.acquire();
		try {
			doUse();
		} finally {
			semaphore.release();
		}
	}

	public void doUse() throws InterruptedException {
		Log.println("Begin: used = " + (permits - semaphore.availablePermits()));
		Thread.sleep(random.nextInt(3000));
		Log.println("End: used = " + (permits - semaphore.availablePermits()));
	}
}
