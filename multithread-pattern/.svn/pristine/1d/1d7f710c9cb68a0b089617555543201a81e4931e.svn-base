package com.thread.ch7_list7_5;

public class Host {
	private final Helper helper = new Helper();

	public void request(final int count, final char c) {
		System.out.println("request " + count + " " + c + ") Begin");
		new Thread(new Runnable() {
			public void run() {
				helper.handle(count, c);
			}
		}).start();
		System.out.println("request " + count + " " + c + ") End");
	}
}
