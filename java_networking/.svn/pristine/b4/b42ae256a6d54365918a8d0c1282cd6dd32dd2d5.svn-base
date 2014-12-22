package com.thread.ch04_exam4_4;

import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.thread.ch04_exam4_4_1.LivenessException;

public class ServerThread extends Thread {
	private final Random random;
	private final RequestQueue requestQueue;

	public ServerThread(RequestQueue requestQueue, String name, long seed,
			long timeout) {
		super(name);
		this.requestQueue = requestQueue;
		this.random = new Random(seed);
		this.requestQueue.timeout(timeout);
	}

	public void run() {
		for (int i = 0; i < 10000; i++) {
			try {
				Request request = requestQueue.getRequest();
				System.out.println("<- " + Thread.currentThread().getName()
						+ " handles " + request);

				Thread.sleep(random.nextInt(3000));
			} catch (LivenessException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
