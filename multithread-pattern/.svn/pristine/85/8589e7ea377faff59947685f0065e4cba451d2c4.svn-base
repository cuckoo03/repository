package com.thread.ch8_exam8_6;

public class WorkerThread extends Thread {
	private final Channel channel;
	private volatile boolean terminated;

	public WorkerThread(String name, Channel channel) {
		super(name);
		this.channel = channel;
	}

	public void run() {
		try {
			while (!terminated) {
				try {
					Request request = channel.takeRequest();
					request.execute();
				} catch (InterruptedException e) {
				}
			}
		} finally {
			System.out.println(Thread.currentThread().getName()
					+ " terminated ");
		}
	}

	public void stopThread() {
		terminated = true;
		// this.interrupt();
	}
}
