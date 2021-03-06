package com.thread.ch06_exam6_4;

import java.util.concurrent.atomic.AtomicInteger;

public class RetrieveThread implements Runnable {
	private final Database<String, String> database;
	private final String key;
	private static final AtomicInteger atomicInteger = new AtomicInteger(0);

	public RetrieveThread(Database<String, String> database, String key) {
		this.database = database;
		this.key = key;
	}

	public void run() {
		while (true) {
			int counter = atomicInteger.incrementAndGet();
			String value = database.retrieve(key);
			System.out.println(counter + ":" + key + " => " + value);
		}
	}

}
