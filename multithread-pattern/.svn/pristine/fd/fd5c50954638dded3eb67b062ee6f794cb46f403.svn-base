package com.thread.ch12_exam12_2;

public class Proxy implements ActiveObject {
	private Servant servant;
	private SchedulerThread scheduler;

	public Proxy(SchedulerThread scheduler, Servant servant) {
		this.scheduler = scheduler;
		this.servant = servant;
	}

	public Result<String> add(String x, String y) {
		FutureResult<String> future = new FutureResult<String>();
		scheduler.invoke(new MakeNumberRequest(servant, future, x, y));
		return future;
	}
}
