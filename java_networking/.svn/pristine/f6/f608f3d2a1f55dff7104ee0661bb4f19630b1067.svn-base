package com.thread.ch10_list10_1;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main begin");
		
		try {
			CountupThread t = new CountupThread();
			t.start();
			
			Thread.sleep(10000);
			
			System.out.println("Main shutdownRequest()");
			t.shutdownRequest();
			
			System.out.println("Main join");
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Main end");
	}
}
