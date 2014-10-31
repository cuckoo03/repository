package com.pecktpub.java7.concurrency.chap4.returnsaresult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Executing tasks in an executor that returns a result
 * @author cuckoo03
 *
 */
public class Main {
	public static void main(String[] args) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(2);
		List<Future<Integer>> resultList = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			Integer number = random.nextInt(10);
			FactorialCalculator cal = new FactorialCalculator(number);
			Future<Integer> result = executor.submit(cal);
			resultList.add(result);
		}

		do {
			System.out.println("Number of compled tasks:"
					+ executor.getCompletedTaskCount());

			for (int i = 0; i < resultList.size(); i++) {
				Future<Integer> result = resultList.get(i);
				System.out.println("Task" + i + "," + result.isDone());
			}
		} while (executor.getCompletedTaskCount() < resultList.size());

		System.out.println("Results");
		for (int i = 0; i < resultList.size(); i++) {
			Future<Integer> result = resultList.get(i);
			Integer number = null;

			try {
				number = result.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			System.out.println("Main Task " + i + "," + number);
		}

		executor.shutdown();
		System.out.println("executor shutdown");
	}

}
