package com.thread.ch05_06;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		Exchanger<char[]> exchanger = new Exchanger<char[]>();
		char[] buffer1 = new char[10];
		char[] buffer2 = new char[10];
		ThreadFactory factory = Executors.defaultThreadFactory();
		factory.newThread(
				new ProducerThread("ProducerThread", exchanger, buffer1, 11111))
				.start();
		factory.newThread(
				new ConsumerThread("ConsumerThread", exchanger, buffer2, 11111))
				.start();
	}

}
