package com.pecktpub.java7.concurrency.chap7.executor;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SleepTwoSecondTask implements Callable<String>{
	@Override
	public String call() throws Exception {
		TimeUnit.SECONDS.sleep(2);
		return new Date().toString();
	}

}
