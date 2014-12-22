package com.thread.ch9_exam9_4;

public class Host {
	public Data request(final int count, final char c) {
		System.out.println(" request(" + count + "," + c + ") begin");

		final FutureData future = new FutureData();

		new Thread() {
			public void run() {
				try {
					RealData realdata = new RealData(count, c);
					future.setRealData(realdata);
				} catch (Exception e) {
					future.setException(e);
				}
			}
		}.start();

		System.out.println(" request(" + count + "," + c + ") end");

		return future;
	}
}
