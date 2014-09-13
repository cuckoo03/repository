package org.concurrent.study.ch06;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.skta.gnm.common.concurrent.SleepThread;

public class BlockingQueueTest {

	public static void main(String[] args) {
		int cpu = Runtime.getRuntime().availableProcessors();
		System.out.println("cpus:" + cpu);
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(5);
		TakeThread take = new TakeThread(queue);
		take.setDelay(100);
		take.start();
		TakeThread take2 = new TakeThread(queue);
		take2.setDelay(100);
		take2.start();

		PutThread offer = new PutThread(queue);
		offer.setDelay(100);
		offer.start();

		try {
			System.out.println("sleep 5 seconds");
			Thread.sleep(5000);
			System.out.println("end 5 seconds");
			offer.finish();
			take.finish();
			take2.finish();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class TakeThread extends SleepThread {
	private BlockingQueue<String> queue;

	public TakeThread(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public void task() throws Exception {
		String item = queue.take();
		System.out.println("take:" + item);
	}
}

class PutThread extends SleepThread {
	private BlockingQueue<String> queue;
	private int count = 0;

	public PutThread(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public void task() throws Exception {
		queue.put(String.valueOf(count++));
		System.out.println("put:" + count);
	}
}