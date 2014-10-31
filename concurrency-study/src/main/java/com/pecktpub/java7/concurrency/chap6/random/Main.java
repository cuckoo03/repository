package com.pecktpub.java7.concurrency.chap6.random;

/**
 * Generating concurrent random numbers
 * @author cuckoo03
 *
 */
public class Main {

	public static void main(String[] args) {
		Thread threads[] = new Thread[3];
		for (int i = 0; i < 3; i++) {
			TaskLocalRandom task  = new TaskLocalRandom();
			threads[i] = new Thread(task);
			threads[i].start();
		}
	}

}
