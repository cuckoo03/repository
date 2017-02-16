package com.effectivejava.ch09;

import java.util.LinkedList;
import java.util.List;

public abstract class WorkQueue {
	private final List queue = new LinkedList();
	private boolean stopped = false;

	protected WorkQueue() {
		new WorkerThread().start();
	}

	public final void enqueue(Object workItem) {
		synchronized (queue) {
			queue.add(workItem);
			queue.notify();
		}
	}

	public final void stop() {
		synchronized (queue) {
			stopped = true;
			queue.notify();
		}
	}

	protected abstract void processItem(Object workItem);

	private class WorkerThread extends Thread {
		@Override
		public void run() {
			while (true) {
				Object workItem = null;
				synchronized (queue) {
					try {
						while (queue.isEmpty() && !stopped)
							queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
					if (stopped)
						return;
					workItem = queue.remove(0);
				}
				processItem(workItem);
			}
		}
	}
}
