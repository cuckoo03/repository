package com.thread.ch7_exam7_8;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

class Log {
	public static void println(String s) {
		System.out.println(Thread.currentThread().getName() + ":" + s);
	}
}

public class Main {
	public static void main(String[] args) {
		Thread.currentThread().setName("MainThread");
		Log.println("Main begin");
		new Executor() {
			public void execute(Runnable command) {
				Log.println("execute begin");
				new ThreadFactory() {
					public Thread newThread(Runnable r) {
						Log.println("newThread begin");
						Thread t = new Thread(r, "quiz");
						Log.println("newThread end");
						return t;
					}
				}.newThread(command).start();
				Log.println("execute end");
			}
		}.execute(new Runnable() {
			public void run() {
				Log.println("run begin");
				Log.println("hello");
				Log.println("run end");
			}
		});
	}
}
