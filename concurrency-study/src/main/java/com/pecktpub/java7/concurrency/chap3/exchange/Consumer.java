package com.pecktpub.java7.concurrency.chap3.exchange;

import java.util.List;
import java.util.concurrent.Exchanger;

public class Consumer implements Runnable {
	private List<String> buffer;
	private final Exchanger<List<String>> exchanger;

	public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
		this.buffer = buffer;
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		int cycle = 1;
		for (int i = 0; i < 10; i++) {
			System.out.println("Consumer: Cycle " + cycle);
			
			try {
				buffer = exchanger.exchange(buffer);
				System.out.println("Consumer received");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Consumer Size " + buffer.size());
			
			for (int j = 0; j < 10; j++) {
				String message = buffer.get(0);
				System.out.println("Consumer get " + message);
				buffer.remove(0);
			}
			cycle++;
		}
	}

}