package com.thread.ch05_06;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ConsumerThread implements Runnable {
	private final String name;
	private final Exchanger<char[]> exchanger;
	private char[] buffer;
	private final Random random;

	public ConsumerThread(String name, Exchanger<char[]> exchanger,
			char[] buffer, int seed) {
		this.name = name;
		this.exchanger = exchanger;
		this.buffer = buffer;
		this.random = new Random(seed);
	}

	public void run() {
		while (true) {
			System.out.println(name + " : " + " before exchange");
			try {
				buffer = exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(name + " : " + " after exchange");

			for (int i = 0; i < buffer.length; i++) {
				System.out.println(name + " : - > " + buffer[i]);
				try {
					Thread.sleep(random.nextInt(1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
