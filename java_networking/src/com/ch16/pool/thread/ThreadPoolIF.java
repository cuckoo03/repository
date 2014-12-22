package com.ch16.pool.thread;

public interface ThreadPoolIF {
	public void addThread();

	public void removeThread();

	public void startAll();

	public void stopAll();
}
