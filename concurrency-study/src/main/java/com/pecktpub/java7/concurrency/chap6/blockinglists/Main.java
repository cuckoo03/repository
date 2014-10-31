package com.pecktpub.java7.concurrency.chap6.blockinglists;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Using blocking thread-safe lists
 * 
 * @author cuckoo03
 *
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		LinkedBlockingDeque<String> list = new LinkedBlockingDeque<String>(3);

		Client client = new Client(list);
		Thread t = new Thread(client);
		t.start();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				String request = list.take();
				System.out.println("Main:Request " + request + " size:"
						+ list.size());
			}
			TimeUnit.MICROSECONDS.sleep(300);
		}
		System.out.println("Main:End");
		
	}
}
