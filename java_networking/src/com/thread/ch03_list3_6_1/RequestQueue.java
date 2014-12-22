package com.thread.ch03_list3_6_1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {
	private final BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

	private int count = 0;

	public Request getRequest() {
		Request request = null;
		try {
			request = queue.take();
		} catch (InterruptedException e) {

		}

		return request;
	}

	public void putRequest(Request request) {
		queue.offer(request);
		System.out.println("queue remain " + queue.size());
	}

	public synchronized int nextValue() {
		return count++;
	}
}
