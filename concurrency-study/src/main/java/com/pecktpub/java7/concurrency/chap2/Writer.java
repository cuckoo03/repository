package com.pecktpub.java7.concurrency.chap2;

public class Writer implements Runnable {
	private PricesInfo pricesInfo;

	public Writer(PricesInfo pricesInfo) {
		this.pricesInfo = pricesInfo;
	}

	public void run() {
		for (int i = 0; i < 3; i++) {
			pricesInfo.setPrices(Math.random() * 10);
			System.out.println("writer prices modified :"
					+ pricesInfo.getPrice1());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
