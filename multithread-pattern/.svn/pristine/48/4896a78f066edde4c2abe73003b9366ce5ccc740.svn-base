package com.thread.ch7_exam7_8;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

class Log2 {
	public static void println(String s) {
		System.out.println(Thread.currentThread().getName() + ":" + s);
	}
}

public class Main2 {
	public static void main(String[] args) {
		Thread.currentThread().setName("MainThread");
		Log.println("Main begin");

		// ThreadFactory
		final ThreadFactory threadFactory = new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Log.println("newThread begin");
				Thread t = new Thread(r, "Quiz");
				Log.println("newThread end");
				return t;
			}
		};

		// Executor
		Executor executor = new Executor() {
			public void execute(Runnable r) {
				Log.println("execute begin");
				threadFactory.newThread(r).start();
				Log.println("execute end");
			}
		};

		// Runnable
		Runnable runnable = new Runnable() {
			public void run() {
				Log.println("run begin");
				Log.println("Hello");
				Log.println("run end");
			}
		};

		executor.execute(runnable);

		Log.println("Main end");
	}
}
