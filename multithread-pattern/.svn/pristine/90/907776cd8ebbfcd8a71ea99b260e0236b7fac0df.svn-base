package com.thread.ch7_list7_10;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main Begin");
		ExecutorService executorService = Executors.newCachedThreadPool();
		Host host = new Host(executorService);
		try {
			host.request(10, 'A');
			host.request(10, 'B');
		} finally {
			executorService.shutdown();
		}
		System.out.println("Main End");
	}

}
