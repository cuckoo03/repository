package com.thread.append.a1;

public class Main {
	public static void main(String[] args) {
		final Something obj = new Something();
		
		new Thread() {
			public void run() {
				obj.write();
			}
		}.start();
		
		new Thread() {
			public void run() {
				obj.read();
			}
		}.start();
	}
}
class Something {
	private int x = 0;
	private int y = 0;
	
	public void write() {
		x = 100;
		y = 50;
	}
	
	public void read() {
		if (x < y) {
			System.out.println("x < y");
		}
	}
}
