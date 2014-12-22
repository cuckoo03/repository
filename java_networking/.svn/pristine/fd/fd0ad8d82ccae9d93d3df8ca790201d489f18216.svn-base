package com.thread.ch7_list7_10;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Host {
	private final Helper helper = new Helper();
	private final Executor executor;

	public Host(Executor executor) {
		this.executor = executor;
	}

	public void request(final int count, final char c) {
		System.out.println("request " + count + " " + c + ") Begin");
		executor.execute(new Runnable() {
			public void run() {
				helper.handle(count, c);
			}
		});
		System.out.println("request " + count + " " + c + ") End");
	}
}
