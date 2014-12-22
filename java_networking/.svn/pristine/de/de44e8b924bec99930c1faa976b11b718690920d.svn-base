package com.thread.ch12_list12_1;

public class SchedulerThread extends Thread {
	private ActivationQueue queue;

	public SchedulerThread(ActivationQueue queue) {
		this.queue = queue;
	}

	public void invoke(MethodRequest request) {
		queue.putRequest(request);
	}
	
	public void run() {
		while (true) {
			MethodRequest request = queue.takeRequest();
			request.execute();
		}
	}
}
