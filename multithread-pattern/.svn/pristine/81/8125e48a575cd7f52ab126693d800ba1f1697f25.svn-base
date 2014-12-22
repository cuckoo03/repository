package com.thread.ch05_01_1;

public class Main {
	public static void main(String[] args) {
		Thread t = null;
		try {
			t = new MyThread();
			t.start();

			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("------end-----");
		t.interrupt();
	}

}
