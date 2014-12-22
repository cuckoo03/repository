package com.thread.ch10_exam10_5_1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service {
	private static GracefulThread thread;

	public static synchronized void service() {
		if ((null != thread) && thread.isAlive()) {
			System.out.println("is balked");
			return;
		}
		System.out.println("service");

		thread = new ServiceThread();
		thread.start();
	}

	public static synchronized void cancel() {
		if (null != thread) {
			System.out.println("cancel");
			thread.shutdownRequest();
		}
	}
}
