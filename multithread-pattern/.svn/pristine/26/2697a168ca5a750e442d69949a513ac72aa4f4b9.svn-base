package com.thread.append.a6;

class Something {
	private final int x;
	private static Something last;

	public Something() {
		x = 123;
		last = this;
	}

	public static void print() {
		if (null != last) {
			System.out.println(last.x);
		}
	}
}

public class Main {
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				new Something();
			}
		}.start();

		new Thread() {
			public void run() {
				Something.print();
			}
		}.start();
	}
}