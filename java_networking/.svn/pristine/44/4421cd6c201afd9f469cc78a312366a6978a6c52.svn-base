package com.thread.ch12_exam12_2;

public class ActiveObjectFactory {
	public static ActiveObject crateActiveObject() {
		Servant servant = new Servant();
		ActivationQueue queue = new ActivationQueue();
		SchedulerThread scheduler = new SchedulerThread(queue);
		Proxy proxy = new Proxy(scheduler, servant);
		scheduler.start();
		return proxy;
	}

}
