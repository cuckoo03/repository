package com.java.inaction.ch11;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

public class Shop {
	private	List<Shop> shops = Arrays.asList(new Shop("a"), new Shop("b"));

	private Executor executor = Executors.newFixedThreadPool(
			Math.min(shops.size(), 100), new ThreadFactory() {
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		}
	});

	private String name;

	public Shop(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	
	public double getPrice(String product) {
		return calculatePrice(product);
	}

	public Future<Double> getPriceAsync(String product) {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
		new Thread(() -> {
			try {
				double price = calculatePrice(product);
				futurePrice.complete(price);
			} catch (Exception e) {
				futurePrice.completeExceptionally(e);
			}
		}).start();
		return futurePrice;
	}

	// using supplyAsync
	public Future<Double> getPriceAsync2(String product) {
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}

	
	public List<String> getPrices(String product) {
		
		List<CompletableFuture<String>> futures = shops
				.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getName()
						+ shop.getPrice(product))).collect(Collectors.toList());
		return futures.stream().map(CompletableFuture::join)
				.collect(Collectors.toList());
	}

	private double calculatePrice(String product) {
		delay();
		return new Random().nextDouble() * product.charAt(0)
				+ product.charAt(1);
	}

	public void delay() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
