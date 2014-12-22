package com.thread.ch04_exam4_5;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		ThreadFactory factory = Executors.defaultThreadFactory();
		Thread thread = factory.newThread(new TestThread());
		while (true) {
			thread.start();
		}
	}
}
