package com.pecktpub.java7.concurrency.chap3.multiplecopies;

public class Job implements Runnable {
	private PrintQueue printQueue;

	public Job(PrintQueue printQueue) {
		this.printQueue = printQueue;
	}

	public void run() {
		System.out.println("start:" + Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.println("end:" + Thread.currentThread().getName());
	}
}
