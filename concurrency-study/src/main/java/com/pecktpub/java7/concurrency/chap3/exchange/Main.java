package com.pecktpub.java7.concurrency.chap3.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * changing data between concurrent tasks
 * @author cuckoo03
 *
 */
public class Main {
	public static void main(String[] args) {
		List<String> buffer1 = new ArrayList<>();
		List<String> buffer2 = new ArrayList<>();
		Exchanger<List<String>> exchanger = new Exchanger<List<String>>();
		Producer producer = new Producer(buffer1, exchanger);
		Consumer consumer = new Consumer(buffer2, exchanger);
		Thread tProducer = new Thread(producer);
		Thread tConsumer = new Thread(consumer);
		
		tProducer.start();
		tConsumer.start();
	}

}
