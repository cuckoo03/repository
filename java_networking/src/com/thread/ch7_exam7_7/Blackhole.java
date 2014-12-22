package com.thread.ch7_exam7_7;

public class Blackhole {
	public static void enter(Object obj) {
		System.out.println("Step 1");
		magic(obj);
		System.out.println("Step 2");
		synchronized (obj) {
			System.out.println("Step 3");
		}
	}

	private static void magic(final Object obj) {
		Thread thread = new Thread() {
			public void run() {
				synchronized (obj) {
					synchronized (this) {
						
						this.notifyAll();
						System.out.println("notifyall");
					}
					while (true) {
					}
				}
			}
		};

		synchronized (thread) {
			thread.start();
			System.out.println("Thread start");
			try {
				System.out.println("Thread wait");
				thread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
