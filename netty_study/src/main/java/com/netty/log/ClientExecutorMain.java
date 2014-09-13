package com.netty.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientExecutorMain {
	private static final int THREADS = 1; 

	public void start() {

	}

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i < THREADS; i++) {
			service.execute(new Runnable() {

				@Override
				public void run() {
					LogCrawlerClient client = new LogCrawlerClient();
				}
			});
		}

		service.shutdown();
	}
}
