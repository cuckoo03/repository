package com.thread.ch05_01_1;

public class MyThread extends Thread {
	private boolean interrupted = false;

	public void run() {
		System.out.println("currentThread interrupted: "
				+ Thread.currentThread().interrupted());
		System.out.println("This Isinterrupted: " + isInterrupted());
		System.out.println("Thread.interrupted: " + Thread.interrupted());
		try {
			while (!isInterrupted()) {
				System.out.println(".");
				Thread.sleep(1);
			}
		} catch (InterruptedException e) {
			System.out.println("currentThread interrupted: "
					+ Thread.currentThread().interrupted());
			System.out.println("This Isinterrupted: " + isInterrupted());
			System.out.println("Thread.interrupted: " + Thread.interrupted());
			interrupted = true;
			e.printStackTrace();
		}
	}
}
