package com.thread.ch10_list10_3;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main begin");

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable exception) {
				System.out.println("****");
				System.out.println("UncaughtException begin");
				System.out.println("currentThread=" + Thread.currentThread());
				System.out.println("thread=" + thread);
				System.out.println("exception" + exception);
				System.out.println("UncaughtException end");
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("****");
				System.out.println("shutdown hook begin");
				System.out.println("currentThread= " + Thread.currentThread());
				System.out.println("shutdown hook end");
			}
		});

		new Thread("MyThread") {
			public void run() {
				System.out.println("MyThread begin");
				System.out.println("MyThread sleep");

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println("MyThread divide");

				int x = 1 / 0;

				System.out.println("MyThread end");
			}
		}.start();

		System.out.println("Main end");
	}

}
