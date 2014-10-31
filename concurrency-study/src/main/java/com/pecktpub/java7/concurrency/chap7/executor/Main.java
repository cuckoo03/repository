package com.pecktpub.java7.concurrency.chap7.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Customizing the ThreadPoolExecutor class
 * 
 * @author cuckoo03
 *
 */
public class Main {

	public static void main(String[] args) {
		MyExecutor executor = new MyExecutor(2, 4, 1000, TimeUnit.MILLISECONDS,
				new LinkedBlockingDeque<Runnable>());
		List<Future<String>> results = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			SleepTwoSecondTask task = new SleepTwoSecondTask();
			Future<String> result = executor.submit(task);
			results.add(result);
		}

		for (int i = 0; i < 5; i++) {
			try {
				String result = results.get(i).get();
				System.out.println("Main:Result for Task:" + i + ",result:"
						+ result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		executor.shutdown();

		for (int i = 5; i < 10; i++) {
			try {
				String result = results.get(i).get();
				System.out.println("Main:Result for Task:" + i + ",result:"
						+ result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Main:end");
	}
}
