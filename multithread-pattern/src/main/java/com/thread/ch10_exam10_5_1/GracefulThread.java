package com.thread.ch10_exam10_5_1;

public class GracefulThread extends Thread {
	private volatile boolean shutdownRequested;

	public final void shutdownRequest() {
		shutdownRequested = true;
		interrupt();
	}

	public final boolean isShutdownRequested() {
		return shutdownRequested;
	}

	public final void run() {
		try {
			while (!isShutdownRequested()) {
				doWork();
			}
		} catch (InterruptedException e) {

		} finally {
			doShutdown();
		}
	}

	protected void doShutdown() {
		// TODO Auto-generated method stub

	}

	protected void doWork() throws InterruptedException {
		// TODO Auto-generated method stub

	}
}
