package com.java.inaction.ch11;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Ch11Main {

	public static void main(String[] args) {
//		seq();
		parrallel();
	}

	private static void seq() {
		Shop shop = new Shop("");
		long start = System.nanoTime();
		Future<Double> futurePrice = shop.getPriceAsync("product");
		long invocationTime = (System.nanoTime() - start) / 1_000_000;
		System.out.println("invocation after " + invocationTime);
		
		doSomethingElse();
		try {
			double price = futurePrice.get();
			System.out.println("price:" + price);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		long retrievalTime = (System.nanoTime() - start) / 1_000_000;
		System.out.println("retrive " + retrievalTime);
	}
	private static void parrallel() {
		Shop shop = new Shop("");
		long start = System.nanoTime();
		long invocationTime = (System.nanoTime() - start) / 1_000_000;
		System.out.println("invocation after " + invocationTime);
		
		doSomethingElse();
		try {
			 List<String> list = shop.getPrices("product");
			System.out.println("price:" + list);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		long retrievalTime = (System.nanoTime() - start) / 1_000_000;
		System.out.println("retrive " + retrievalTime);	
	}

	private static void doSomethingElse() {
		System.out.println("doSomethingElse");
	}
}
