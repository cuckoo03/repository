package com.thread.ch7_exam7_2;

public class Host {
	private final Helper helper = new Helper();

	public void request(final int count, final char c) {
		System.out.println("request " + count + " " + c + ") Begin");
		new Thread() {
			public void run() {
				helper.handle(count, c);
			}
		}.start();
		System.out.println("request " + count + " " + c + ") End");
	}
}
