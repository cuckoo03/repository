package com.thread.ch06_exam6_4;

import java.util.Random;

public class AssignThread implements Runnable {
	private static Random random = new Random(11111);
	private Database<String, String> database;
	private final String key;
	private final String value;

	public AssignThread(Database<String, String> database, String key,
			String value) {
		this.database = database;
		this.key = key;
		this.value = value;
	}

	public void run() {
		while (true) {
			System.out.println(Thread.currentThread().getName() + ":assign("
					+ key + ", " + value + ")");
			database.assign(key, value);
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {

			}
		}
	}

}
