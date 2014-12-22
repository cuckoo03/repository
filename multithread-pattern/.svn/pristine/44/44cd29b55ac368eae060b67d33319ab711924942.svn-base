package com.thread.ch01.exam06;

import java.util.Random;
import java.util.concurrent.Semaphore;

class Log {
	public static void println(String s) {
		System.out.println(Thread.currentThread().getName() + ":" + s);
	}
}

class BoundedResource {
	private final Semaphore semaphore;
	private final int permits;
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

	private void doUse() throws InterruptedException {
		Log.println("begin: used=" + (permits - semaphore.availablePermits()));
		Thread.sleep(random.nextInt(1000));
		Log.println("end: used=" + (permits - semaphore.availablePermits()));
	}
}

class List1_11UserThread extends Thread {
	private final static Random random = new Random(26535);
	private final BoundedResource resource;

	public List1_11UserThread(BoundedResource resource) {
		this.resource = resource;
	}

	public void run() {
		try {
			while (true) {
				resource.use();
				Thread.sleep(random.nextInt(5000));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

public class List1_11Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BoundedResource resource = new BoundedResource(2);
		for (int i = 0; i < 3; i++) {
			new List1_11UserThread(resource).start();
		}
	}
}
