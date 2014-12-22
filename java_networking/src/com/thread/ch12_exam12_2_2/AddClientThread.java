package com.thread.ch12_exam12_2_2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class AddClientThread extends Thread {
	private String x = "a";
	private String y = "a";
	private ActiveObject activeObject;

	public AddClientThread(String name, ActiveObject activeObject) {
		super(name);
		this.activeObject = activeObject;
	}

	public void run() {
		try {
			while (true) {
				Future<String> future = activeObject.add(x, y);
				Thread.sleep(100);
				String z = future.get();
				System.out.println(Thread.currentThread().getName() + ":" + x
						+ "+" + y + "=" + z);

				x = y;
				y = z;
			}
		} catch (RejectedExecutionException e) {
			System.out.println(Thread.currentThread().getName() + ":" + e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
