package com.thread.ch7_list7_12;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Host {
	private final Helper helper = new Helper();
	private final ScheduledExecutorService executor;

	public Host(ScheduledExecutorService executor) {
		this.executor = executor;
	}

	public void request(final int count, final char c) {
		System.out.println("request " + count + " " + c + ") Begin");
		executor.schedule(new Runnable() {
			public void run() {
				helper.handle(count, c);
			}
		}, 3L, TimeUnit.SECONDS);
		System.out.println("request " + count + " " + c + ") End");
	}
}
