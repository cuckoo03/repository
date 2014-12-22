package com.thread.ch05_06;

import java.util.Random;
import java.util.concurrent.Exchanger;

public class ProducerThread implements Runnable {
	private final String name;
	private final Exchanger<char[]> exchanger;
	private char[] buffer = null;
	private int index = 0;
	private final Random random;

	public ProducerThread(String name, Exchanger<char[]> exchanger,
			char[] buffer, int seed) {
		this.name = name;
		this.exchanger = exchanger;
		this.buffer = buffer;
		this.random = new Random(seed);
	}

	public void run() {
		try {
			while (true) {
				for (int i = 0; i < buffer.length; i++) {
					buffer[i] = nextChar();
					System.out.println(name + " : " + buffer[i] + " -> ");
				}

				System.out.println(name + " : " + " before Exchange");
				buffer = exchanger.exchange(buffer);
				System.out.println(name + " : " + " after Exchange");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private char nextChar() throws InterruptedException {
		char c = (char) ('A' + index % 26);
		index++;
		Thread.sleep(random.nextInt(1000));

		return c;
	}
}