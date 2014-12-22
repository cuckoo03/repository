package com.thread.ch12_exam12_2_2;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ActiveObjectImpl implements ActiveObject {
	private ExecutorService service = Executors.newSingleThreadExecutor();

	public void shutdown() {
		service.shutdown();
	}

	public Future<String> add(final String x, final String y) {
		class NumberRequest implements Callable<String> {
			public String call() throws Exception {
				String retvalue = null;
				try {
					BigInteger xx = new BigInteger(x);
					BigInteger yy = new BigInteger(y);
					BigInteger zz = xx.add(yy);
					retvalue = zz.toString();
				} catch (NumberFormatException e) {
					retvalue = null;
				}
				return retvalue;
			}
		}
		return service.submit(new NumberRequest());
	}
}