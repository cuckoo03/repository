package com.pecktpub.java7.concurrency.chap3.waitingevents;

import java.util.concurrent.CountDownLatch;

public class VideoConference implements Runnable {
	private final CountDownLatch controller;

	public VideoConference(int number) {
		controller = new CountDownLatch(number);
	}

	@Override
	public void run() {
		System.out.println("init:" + controller.getCount());

		try {
			controller.await();
			System.out.println("Await");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void arrive(String name) {
		System.out.printf("%s has arrived\n", name);
		controller.countDown();
		System.out.printf(name + " Waiting %d \n",
				controller.getCount());
	}
}
