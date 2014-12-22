package com.thread.ch04;

public class MultiLockingThreads {
	public static void main(String[] args) {
		for (int i = 0; i < 6; i++) {
			LockThread thread = new LockThread();
			thread.start();
		}
	}
}

class LockThread extends Thread {
	public void run() {
		while (true) {
			IncreaseNumber.increase();
		}
	}
}

class IncreaseNumber {
	private static long count = 0;

	public static synchronized void increase() {
		count++;
	}
}