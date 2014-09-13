package org.concurrent.study.ch06;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {

	public static void main(String[] args) {
		FutureData future = new FutureData(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep(3000);
				return "realdata";
			}
		});
		System.out.println("future complete");

		new Thread(future).start();
		
		String data = null;
		try {
			System.out.println("before get data");
			data = future.get();
			System.out.println("after get data");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println(data);
	}
}

class FutureData extends FutureTask<String> {
	public FutureData(Callable<String> callable) {
		super(callable);
	}
}
