package com.pecktpub.java7.concurrency.chap8.tc;

import edu.umd.cs.mtc.TestFramework;

public class Main {

	public static void main(String[] args) throws Throwable {
		ProducerConsumerTest test = new ProducerConsumerTest();
		
		System.out.println("Main:Starting");
		TestFramework.runOnce(test);
		System.out.println("Main:Finished");
	}

}
