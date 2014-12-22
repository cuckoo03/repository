package com.ch16.pool.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ch16.queue.Queue;
import com.ch16.utils.DynamicClassLoader;

public class ThreadPool implements ThreadPoolIF {
	private int max = 10;
	private int min = 2;
	private int current = 0;
	private final Object monitor = new Object();
	private final List<Thread> pool = new ArrayList<Thread>();

	private Queue queue = null;
	private String type = null;

	public ThreadPool(Queue queue, String type) {
		this(queue, type, 2, 10);
	}

	public ThreadPool(Queue queue, String type, int min, int max) {
		this.queue = queue;
		this.type = type;
		this.min = min;
		this.max = max;
		init();
	}

	private void init() {
		for (int i = 0; i < min; i++) {
			pool.add(createThread());
		}
	}

	private synchronized Thread createThread() {
		Thread thread = null;
		try {
			thread = (Thread) DynamicClassLoader.createInstance(type, queue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		current++;
		return thread;
	}

	public void addThread() {
		synchronized (monitor) {
			if (current < max) {
				Thread t = createThread();
				t.start();
				pool.add(t);
			}
		}
	}

	public void removeThread() {
		synchronized (monitor) {
			if (current > min) {
				Thread t = (Thread) pool.remove(0);
				t.interrupt();
				t = null;
			}
		}
	}

	public void startAll() {
		synchronized (monitor) {
			Iterator<Thread> iter = pool.iterator();
			while (iter.hasNext()) {
				Thread thread = iter.next();
				thread.start();
			}
		}
	}

	public void stopAll() {
		synchronized (monitor) {
			Iterator<Thread> iter = pool.iterator();
			while (iter.hasNext()) {
				Thread thread = iter.next();
				thread.interrupt();
				thread = null;
			}
			pool.clear();
		}
	}
}