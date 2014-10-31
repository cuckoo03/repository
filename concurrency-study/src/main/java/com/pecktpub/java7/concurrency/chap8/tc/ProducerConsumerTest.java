package com.pecktpub.java7.concurrency.chap8.tc;

import java.util.concurrent.LinkedTransferQueue;

import edu.umd.cs.mtc.MultithreadedTestCase;

public class ProducerConsumerTest extends MultithreadedTestCase {
	private LinkedTransferQueue<String> queue;

	@Override
	public void initialize() {
		super.initialize();
		queue = new LinkedTransferQueue<String>();
		System.out.println("init");
	}

	public void thread1() throws InterruptedException {
		String ret = queue.take();
		System.out.println("Thread1:" + ret);
	}

	public void thread2() throws InterruptedException {
		 waitForTick(1);
		String ret = queue.take();
		System.out.println("Thread2:" + ret);
	}

	public void thread3() {
		queue.put("Event1");
		queue.put("Event2");
		System.out.println("Thread3:Inserted two elements");
	}

	public void thread4() {
		waitForTick(4);
		System.out.println("thread4");
	}
	public void finish() {
		super.finish();
		System.out.println("Test");
		assertEquals(true, queue.size() == 0);
		System.out.println("Test Result:The queue is empty");
	}
}
