package com.pecktpub.java7.concurrency.chap6.nonblockinglist;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Using non-blocking thread-safe lists
 * 
 * @author cuckoo03
 *
 */
public class Main {
	public static void main(String[] args) {
		ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<String>();
		Thread threads[] = new Thread[100];

		for (int i = 0; i < threads.length; i++) {
			AddTask task = new AddTask(list);
			threads[i] = new Thread(task);
			threads[i].start();
		}
		System.out.println("Main:AddTask threads have been launched "
				+ threads.length);

		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Main:Size of the list:" + list.size());

		for (int i = 0; i < threads.length; i++) {
			PollTask task = new PollTask(list);
			threads[i] = new Thread(task);
			threads[i].start();
		}
		System.out.println("Main:PollTask threads have been launched "
				+ threads.length);

		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Main:size of the list:" + list.size());
	}
}
