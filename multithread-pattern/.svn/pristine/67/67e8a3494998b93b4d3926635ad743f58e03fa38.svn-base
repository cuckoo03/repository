package com.thread.ch04_exam4_4;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

import com.thread.ch04_exam4_4_1.LivenessException;

public class RequestQueue {
	private final Queue<Request> queue = new LinkedList<Request>();
	private long timeout;

	public void timeout(long timeout) {
		this.timeout = timeout;
	}

	public synchronized Request getRequest() throws LivenessException {
		long start = System.currentTimeMillis();
		while (queue.peek() == null) {
			try {
				System.out.println("== " + Thread.currentThread().getName()
						+ " wait begin, queue= " + queue);

				long now = System.currentTimeMillis();
				long rest = timeout - (now - start);
				if (0 >= rest) {
					throw new LivenessException("now - start = "
							+ (now - start) + ",  timeout = " + timeout);

				}
				System.out.println("== " + Thread.currentThread().getName()
						+ " waiting  " + queue);
				wait(rest);

				System.out.println("== " + Thread.currentThread().getName()
						+ " wait end, queue= " + queue);
			} catch (InterruptedException e) {

			}
		}
		System.out.println("remove");
		return queue.remove();
	}

	public synchronized void putRequest(Request request) {
		queue.offer(request);
		System.out.println(Thread.currentThread().getName()
				+ " notifyAll begin, queue= " + queue);

		notifyAll();

		System.out.println(Thread.currentThread().getName()
				+ " notifyAll end, queue= " + queue);
	}
}
