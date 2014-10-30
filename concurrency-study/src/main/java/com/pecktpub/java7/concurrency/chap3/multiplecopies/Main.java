package com.pecktpub.java7.concurrency.chap3.multiplecopies;

/**
 * controlling cuoncurrent access to multiple copies of a resource
 * @author cuckoo03
 *
 */
public class Main {

	public static void main(String[] args) {
		final int THREADS = 10;
		PrintQueue printQueue = new PrintQueue();
		Thread thread[] = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++) {
			thread[i] = new Thread(new Job(printQueue), "Thread" + i);
		}

		for (int i = 0; i < THREADS; i++) {
			thread[i].start();
		}
	}

}
