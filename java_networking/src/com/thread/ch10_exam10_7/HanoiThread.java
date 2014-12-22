package com.thread.ch10_exam10_7;

public class HanoiThread extends Thread {
	private volatile boolean shutdownRequested = false;
	private volatile long requestedTimeMillis = 0;

	public void shutdownRequest() {
		requestedTimeMillis = System.currentTimeMillis();
		shutdownRequested = true;
		interrupt();
	}

	public boolean isShutdownRequested() {
		return shutdownRequested;
	}

	public void run() {
		try {
			for (int level = 0; !isShutdownRequested(); level++) {
				System.out.println("==== level " + level + " ====");
				doWork(level, 'A', 'B', 'C');
				System.out.println("");
			}
		} catch (InterruptedException e) {
		} finally {
			doShutdown();
		}
	}

	private void doShutdown() {
		long time = System.currentTimeMillis() - requestedTimeMillis;
		System.out.println("doShutdown: latency=" + time + "mesc");
	}

	private void doWork(int level, char posA, char posB, char posC)
			throws InterruptedException {
		if (level > 0) {
			if (isShutdownRequested()) {
				throw new InterruptedException();
			}
			doWork(level - 1, posA, posB, posC);
			System.out.print(posA + "->" + posB + " ");
			doWork(level - 1, posC, posB, posA);
		}
	}
}
