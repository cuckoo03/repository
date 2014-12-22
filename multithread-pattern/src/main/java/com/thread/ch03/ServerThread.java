package com.thread.ch03;

import java.util.Random;

public class ServerThread extends Thread {
	private final Random random;
	private final RequestQueue requestQueue;

	public ServerThread(RequestQueue requestQueue, String name, long seed) {
		super(name);
		this.requestQueue = requestQueue;
		this.random = new Random(seed);
	}

	public void run() {
		for (int i = 0; i < 10000; i++) {
			try {
				Request request = requestQueue.getRequest();
				System.out.println("<- " + Thread.currentThread().getName()
						+ " handles " + request);
				Thread.sleep(random.nextInt(3000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
