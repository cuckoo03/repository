package com.thread.ch10_exam10_2;

public class CountupThread extends Thread {
	private long counter = 0;
//	private volatile boolean shutdownRequested;

	public void run() {
		while (!isInterrupted()/*!isShutdownRequested()*/) {
			try {
				doWork();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				doShutdown();
			}
		}
	}

	public void shutdownRequest() {
//		shutdownRequested = true;
		interrupt();
//		Thread.currentThread().interrupt();
	}

	/*
	public boolean isShutdownRequested() {
		return shutdownRequested;
	}
	*/

	private void doShutdown() {
		System.out.println("doShutdown: counter = " + counter);
	}

	private void doWork() throws InterruptedException {
		counter++;
		System.out.println("doWork counter = " + counter);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
