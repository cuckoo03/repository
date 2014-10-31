package com.pecktpub.java7.concurrency.chap8.lock;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

public class MyLock extends ReentrantLock{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getOwnerName() {
		if (this.getOwner() == null) {
			return "None";
		}
		return this.getOwner().getName();
	}
	
	public Collection<Thread> getThreads() {
		return this.getQueuedThreads();
	}
}
