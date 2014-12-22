package com.thread.ch9_exam9_3;

public class Retriever {
	public static Content retrieve(final String string) {
		 final ASyncContentImpl future = new ASyncContentImpl();
		new Thread() {
			public void run() {
				SyncContentImpl realdata = new SyncContentImpl(string);
				future.setRealData(realdata);
			}
		}.start();
		return future;
	}
	/*
	public static Content retrieve(final String string) {
		return new SyncContentImpl(string);
	}
	*/
}
