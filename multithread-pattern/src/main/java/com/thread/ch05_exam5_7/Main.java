package com.thread.ch05_exam5_7;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		ThreadFactory factory = Executors.defaultThreadFactory();
		Thread thread = factory.newThread(new HostThread(3));
		thread.start();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("interrupt");
		thread.interrupt();
	}
}
