package com.thread.ch05_exam5_7;

public class HostThread extends Thread {
	private int count;

	public HostThread(int count) {
		this.count = count;
	}

	public void run() {
		try {
			execute(count);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void execute(int count) throws InterruptedException {
		for (int i = 0; i < count; i++) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			doHeavyJob();
		}
	}

	private static void doHeavyJob() {
		System.out.println("doHeavyJob begin");
		long start = System.currentTimeMillis();
		while (start + 5000 > System.currentTimeMillis()) {
		}

		System.out.println("doHeavyJob end");
	}
}
