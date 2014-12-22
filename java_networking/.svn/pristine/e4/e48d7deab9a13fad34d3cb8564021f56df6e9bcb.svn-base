package com.thread.ch9_exam9_3_1;

import java.util.concurrent.Callable;

public class Retriever {
	public static Content retrieve(final String string) {
		ASyncContentImpl future = new ASyncContentImpl(
				new Callable<SyncContentImpl>() {
					public SyncContentImpl call() {
						return new SyncContentImpl(string);
					}
				});
		new Thread(future).start();

		return future;
	}
}
