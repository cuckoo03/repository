package com.thread.ch9_list9_6;

import java.util.concurrent.Callable;

public class Host {
	public Data request(final int count, final char c) {
		System.out.println(" request(" + count + "," + c + ") begin");

		FutureData future = new FutureData(new Callable<RealData>() {
			public RealData call() {
				return new RealData(count, c);
			}
		});
		
		new Thread(future).start();

		System.out.println(" request(" + count + "," + c + ") end");

		return future;
	}

}
