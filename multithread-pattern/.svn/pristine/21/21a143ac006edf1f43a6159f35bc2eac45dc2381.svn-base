package com.thread.chap01_2;

import java.util.Random;

public class UserThread implements Runnable {
	private static Random random = new Random(26535);
	private BoundedResource resource;

	public UserThread(BoundedResource resource) {
		this.resource = resource;
	}

	public void run() {
		try {
			while (true) {
				resource.use();
				Thread.sleep(random.nextInt(5000));
			}
		} catch (InterruptedException e) {

		}
	}
}
