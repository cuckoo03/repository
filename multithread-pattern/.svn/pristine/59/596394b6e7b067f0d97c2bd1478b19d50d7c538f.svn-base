package com.thread.ch10_exam10_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service {

	private static ServiceThread thread;
	 
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
		thread.shutdown();
	}
}
