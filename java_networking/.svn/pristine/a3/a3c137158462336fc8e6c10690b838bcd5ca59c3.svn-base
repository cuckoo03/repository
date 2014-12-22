package com.thread.ch9_exam9_3_1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ASyncContentImpl extends FutureTask<SyncContentImpl> implements
		Content {
	public ASyncContentImpl(Callable<SyncContentImpl> callable) {
		super(callable);
	}

	public synchronized byte[] getBytes() {
		byte[] content = null;
		try {
			content = get().getBytes();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return content;
	}
}
