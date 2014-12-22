package com.ch16.queue;

import java.util.ArrayList;
import java.util.List;

import com.ch16.events.Job;
import com.ch16.events.NIOEvent;

public class BlockingEventQueue implements Queue {
	private final Object acceptMonitor = new Object();
	private final Object readMonitor = new Object();

	private final List<Job> acceptQueue = new ArrayList<Job>();
	private final List<Job> readQueue = new ArrayList<Job>();

	private static BlockingEventQueue instance = new BlockingEventQueue();

	public static Queue getInstance() {
		if (null == instance) {
			synchronized (BlockingEventQueue.class) {
				instance = new BlockingEventQueue();
			}
		}
		return instance;
	}

	private BlockingEventQueue() {
	}

	public Job pop(int eventType) {
		switch (eventType) {
		case NIOEvent.ACCEPT_EVENT:
			return getAcceptJob();
		case NIOEvent.READ_EVENT:
			return getReadJob();
		default:
			throw new IllegalArgumentException("illegal eventtype");
		}
	}

	private Job getReadJob() {
		synchronized (readMonitor) {
			if (readQueue.isEmpty()) {
				try {
					System.out.println("getReadJob:readQueue is empty");
					readMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return readQueue.remove(0);
		}
	}

	private Job getAcceptJob() {
		synchronized (acceptMonitor) {
			if (acceptQueue.isEmpty()) {
				try {
					System.out.println("getAcceptJob:acceptQueue is empty");
					acceptMonitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return acceptQueue.remove(0);
		}
	}

	public void push(Job job) {
		if (null != job) {
			int eventType = job.getEventType();
			switch (eventType) {
			case NIOEvent.ACCEPT_EVENT:
				putAcceptJob(job);
				break;
			case NIOEvent.READ_EVENT:
				putReadJob(job);
				break;
			default:
				throw new IllegalArgumentException("illegal eventtype");
			}
		}
	}

	private void putReadJob(Job job) {
		synchronized (readMonitor) {
			readQueue.add(job);
			readMonitor.notify();
		}
	}

	private void putAcceptJob(Job job) {
		synchronized (acceptMonitor) {
			acceptQueue.add(job);
			acceptMonitor.notify();
		}
	}
}
