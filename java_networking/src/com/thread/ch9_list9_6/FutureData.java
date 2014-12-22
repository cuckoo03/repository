package com.thread.ch9_list9_6;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureData extends FutureTask<RealData> implements Data {
	public FutureData(Callable<RealData> callable) {
		super(callable);
	}

	public String getContent() {
		String string = null;
		try {
			string = get().getContent();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}

		return string;
	}
}
