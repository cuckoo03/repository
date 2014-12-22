package com.thread.ch7_list7_8;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Host {
	private final Helper helper = new Helper();
	private final ThreadFactory factory;

	public Host(ThreadFactory threadFactory) {
		this.factory = threadFactory;
	}
	
	public void request(final int count, final char c) {
		System.out.println("request " + count + " " + c + ") Begin");
		factory.newThread(new Runnable() {
			public void run() {
				helper.handle(count, c);
			}
		}).start();
		System.out.println("request " + count + " " + c + ") End");
	}
}
