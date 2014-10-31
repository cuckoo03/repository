package com.pecktpub.java7.concurrency.chap6.delayed;

import java.util.Date;
import java.util.concurrent.DelayQueue;

public class Task implements Runnable {
	private int id;
	private DelayQueue<Event> queue;

	public Task(int id, DelayQueue<Event> queue) {
		this.id = id;
		this.queue = queue;
	}

	@Override
	public void run() {
		Date now = new Date();
		Date delay = new Date();
		delay.setTime(now.getTime() + (id * 1000));
		System.out.println("Thread:" + id + "," + delay);
		
		for (int i = 0; i < 10; i++) {
			Event e = new Event(delay);
			queue.add(e);
		}
	}
}
