package com.pecktpub.java7.concurrency.chap2;

public class Main {

	public static void main(String[] args) {
		PricesInfo pricesInfo = new PricesInfo();
		Reader readers[] = new Reader[3];
		Thread threadReader[] = new Thread[3];

		for (int i = 0; i < 3; i++) {
			readers[i] = new Reader(pricesInfo);
			threadReader[i] = new Thread(readers[i]);
		}

		Writer writer = new Writer(pricesInfo);
		Thread threadWriter = new Thread(writer);

		for (int i = 0; i < 3; i++) {
			threadReader[i].start();
		}
		threadWriter.start();
	}
}
