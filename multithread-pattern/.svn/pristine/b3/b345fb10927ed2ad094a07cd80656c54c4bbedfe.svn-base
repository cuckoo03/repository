package com.thread.append.a2;

class Runner extends Thread {
	private boolean quit = false;
	
	public void run() {
		while (!quit) {
		System.out.print(".");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		System.out.println("done");
	}
	
	public void shutdown() {
		quit = true;
	}
}
public class Main {
	public static void main(String[] args) {
		Runner runner = new Runner();
		runner.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("main end");
		runner.shutdown();
	}

}
