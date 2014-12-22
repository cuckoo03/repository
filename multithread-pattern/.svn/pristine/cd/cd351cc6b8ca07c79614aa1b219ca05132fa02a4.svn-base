package com.thread.ch9_exam9_4_1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureData extends FutureTask<RealData> implements Data {
	public FutureData(Callable<RealData> callable) {
		super(callable);
	}

	public String getContent() throws ExecutionException {
		String string = null;
		try {
			string = get().getContent();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		return string;
	}
}
