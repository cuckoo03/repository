package com.thread.ch04_exam4_4;

import java.util.Random;

public class ClientThread extends Thread {
	private final Random random;
	private final RequestQueue requestQueue;

	public ClientThread(RequestQueue requestQueue, String name, long seed,
			long timeout) {
		super(name);
		this.requestQueue = requestQueue;
		this.random = new Random(seed);

		this.requestQueue.timeout(timeout);
	}

	public void run() {
		for (int i = 0; i < 10000; i++) {
			Request request = new Request("No." + i);
			System.out.println("-> " + Thread.currentThread().getName()
					+ " requests " + request);
			requestQueue.putRequest(request);
			try {
				Thread.sleep(random.nextInt(4000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
