package com.thread.ch8_list8_7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		ThreadFactory factory = Executors.defaultThreadFactory();
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		try {
			factory.newThread(new ClientThread("A", executorService)).start();
			factory.newThread(new ClientThread("B", executorService)).start();
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
	}

}
