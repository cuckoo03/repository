package com.thread.ch10_exam10_4;

public class CountupThread extends GracefulThread {
	private long counter = 0;

	@Override
	protected void doShutdown() {
		System.out.println("doShutdown: counter = " + counter);
	}

	@Override
	protected void doWork() throws InterruptedException {
		counter++;
		System.out.println("doWork counter = " + counter);
		Thread.sleep(1000);
	}
}
