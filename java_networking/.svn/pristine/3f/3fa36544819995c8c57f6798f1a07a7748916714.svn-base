package com.thread.ch10_exam10_5;

public class ServiceThread extends Thread {
	private volatile boolean shutdownRequested;

	public void run() {
		try {
			doWork();
		} catch (InterruptedException e) {
			System.out.println("cancel");
		} finally {
			System.out.println("done");
		}
	}

	public void shutdown() {
		shutdownRequested = true;
		interrupt();
	}

	private void doWork() throws InterruptedException {
		for (int i = 0; i < 50; i++) {
			if (shutdownRequested) {
				throw new InterruptedException();
			}
			System.out.print(".");

			Thread.sleep(100);
		}
	}

	public void restart() {
		shutdownRequested = false;
	}
}
