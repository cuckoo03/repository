package com.pecktpub.java7.concurrency.chap5.forkjoinpool;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) throws Exception {
		ProductListGenerator gen = new ProductListGenerator();
		List<Product> products = gen.generate(10);
		Task task = new Task(products, 0, products.size(), 0.20);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);

		do {
			System.out.println("Main:Thread count:"
					+ pool.getActiveThreadCount());
			System.out.println("Main:Thread Steel:" + pool.getStealCount());
			System.out.println("Main:Parallelism:" + pool.getParallelism());
			TimeUnit.MILLISECONDS.sleep(5);
		} while (!task.isDone());

		pool.shutdown();

		if (task.isCompletedNormally()) {
			System.out.println("Main:the process has complete");
		}
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			if (product.getPrice() != 12) {
				System.out.println("Product:" + product.getName() + ","
						+ product.getPrice());
			}
		}
		
		System.out.println("Main:End");
	}
}