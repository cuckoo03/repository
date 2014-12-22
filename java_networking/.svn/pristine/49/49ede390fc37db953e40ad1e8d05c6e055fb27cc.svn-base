package com.thread.ch7_exam7_4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Host {
	private final Helper helper = new Helper();
	private final ExecutorService executorService = Executors
			.newCachedThreadPool();

	public void request(final int count, final char c) {
		System.out.println("request " + count + " " + c + ") Begin");
		executorService.execute(new HelperThread(helper, count, c));
		System.out.println("request " + count + " " + c + ") End");
	}
}
