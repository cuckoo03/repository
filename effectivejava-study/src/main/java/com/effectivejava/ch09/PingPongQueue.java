package com.effectivejava.ch09;

public class PingPongQueue extends WorkQueue {
	@Override
	protected void processItem(Object workItem) {
		System.out.println("processItem()");
	}

	public static void main(String[] args) {
		PingPongQueue q1 = new PingPongQueue();
		q1.enqueue("1");
//		q1.enqueue("2");
		try {
			System.out.println("sleep start");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("sleep end");
		q1.stop();
	}
}
