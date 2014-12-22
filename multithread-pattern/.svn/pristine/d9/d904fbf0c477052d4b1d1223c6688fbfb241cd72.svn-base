package com.thread.ch03;

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue {
	private final Queue<Request> queue = new LinkedList<Request>();

	public synchronized Request getRequest() throws InterruptedException {
		while (queue.peek() == null) {
			try {
				System.out.println("== " + Thread.currentThread().getName()
						+ " wait begin, queue= " + queue);

				wait();

				System.out.println("== " + Thread.currentThread().getName()
						+ " waiting  " + queue);
				
				Thread.sleep(100);
				
				System.out.println("== " + Thread.currentThread().getName()
						+ " wait end, queue= " + queue);
			} catch (InterruptedException e) {

			}
		}
		System.out.println("remove");
		return queue.remove();
	}

	public synchronized void putRequest(Request request) throws InterruptedException {
		queue.offer(request);
		System.out.println(Thread.currentThread().getName()
				+ " notifyAll begin, queue= " + queue);

		notifyAll();

		System.out.println(Thread.currentThread().getName()
				+ " notifyAll end, queue= " + queue);
	}
}
