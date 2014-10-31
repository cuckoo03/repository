package com.pecktpub.java7.concurrency.chap6.priority;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Using blocking thread-safe lists ordered by priority
 * 
 * @author cuckoo03
 *
 */
public class Main {

	public static void main(String[] args) {
		PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<Event>();

		Thread taskThreads[] = new Thread[5];
		for (int i = 0; i < taskThreads.length; i++) {
			Task t = new Task(i, queue);
			taskThreads[i] = new Thread(t);
		}

		for (int i = 0; i < taskThreads.length; i++) {
			taskThreads[i].start();
		}

		for (int i = 0; i < taskThreads.length; i++) {
			try {
				taskThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Main:Queue size:" + queue.size());
		for (int i = 0; i < taskThreads.length * 1000; i++) {
			Event e = queue.poll();
			System.out.println("Thread:" + e.getThread() + " Priority:"
					+ e.getPriority());
		}
		
		System.out.println("Main:Queue size:" + queue.size());
		System.out.println("Main:End");
	}

}
