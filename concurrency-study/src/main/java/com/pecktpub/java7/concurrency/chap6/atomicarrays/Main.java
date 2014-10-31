package com.pecktpub.java7.concurrency.chap6.atomicarrays;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Using Atomic arrays
 * 
 * @author cuckoo03
 *
 */
public class Main {

	public static void main(String[] args) {
		final int THREADS = 10;
		AtomicIntegerArray vector = new AtomicIntegerArray(1000);

		Incrementer inc = new Incrementer(vector);
		Decrementer dec = new Decrementer(vector);

		Thread threadInc[] = new Thread[THREADS];
		Thread threadDec[] = new Thread[THREADS];

		for (int i = 0; i < THREADS; i++) {
			threadInc[i] = new Thread(inc);
			threadDec[i] = new Thread(dec);

			threadInc[i].start();
//			threadDec[i].start();
		}

		for (int i = 0; i < THREADS; i++) {
			try {
				threadInc[i].join();
//				threadDec[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < vector.length(); i++) {
			if (vector.get(i) != 0) {
				System.out.println("Vector[" + i + "]=" + vector.get(i));
			}
		}
		
		System.out.println("Main:end");
	}

}
