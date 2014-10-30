package com.pecktpub.java7.concurrency.chap2;

public class Reader implements Runnable {
	private PricesInfo pricesInfo;

	public Reader(PricesInfo pricesInfo) {
		this.pricesInfo = pricesInfo;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println("price : " + Thread.currentThread().getName()
					+ " : " + pricesInfo.getPrice1());
		}
	}

}
