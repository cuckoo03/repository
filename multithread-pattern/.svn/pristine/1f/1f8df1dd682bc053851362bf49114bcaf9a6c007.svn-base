package com.thread.ch10_exam10_8_1;

public class Main {
	public static void main(String[] args) {
		Thread t = new Thread() {
			public void run() {
				while (true) {
					try {
						if (Thread.interrupted()) {
							throw new InterruptedException();
						}
						System.out.print(".");
						Thread.sleep(100);
					} catch (InterruptedException e) {
						System.out.print("*");
					}
				}
			}
		};

		t.start();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		t.interrupt();
	}

}
