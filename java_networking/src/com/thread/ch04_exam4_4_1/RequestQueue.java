package com.thread.ch04_exam4_4_1;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RequestQueue {
	public RequestQueue(long timeout) {
		this.timeout = timeout;
	}

	private LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

	private long timeout = 0;

	public Request getRequest() {
		Request request = null;
		try {
			request = queue.poll(timeout, TimeUnit.SECONDS);
			if (null == request) {
				throw new LivenessException("thrown by "
						+ Thread.currentThread().getName());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return request;
	}

	public void putRequest(Request request) {
		try {
			boolean offered = queue.offer(request, timeout, TimeUnit.SECONDS);
			if (!offered) {
				throw new LivenessException("thrown by "
						+ Thread.currentThread().getName());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
