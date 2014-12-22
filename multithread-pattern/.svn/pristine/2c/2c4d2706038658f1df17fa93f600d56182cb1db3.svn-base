package com.thread.ch06_exam6_4;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		Database<String, String> database = new Database<String, String>();
		ThreadFactory factory = Executors.defaultThreadFactory();
		factory.newThread(new AssignThread(database, "A", "A1")).start();
		factory.newThread(new AssignThread(database, "A", "A2")).start();
		factory.newThread(new AssignThread(database, "B", "B1")).start();
		factory.newThread(new AssignThread(database, "B", "B2")).start();

		for (int i = 0; i < 100; i++) {
			factory.newThread(new RetrieveThread(database, "A")).start();
			factory.newThread(new RetrieveThread(database, "B")).start();
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("exit---------------");
		System.exit(0);
	}
}
