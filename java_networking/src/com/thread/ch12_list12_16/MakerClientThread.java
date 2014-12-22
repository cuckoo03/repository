package com.thread.ch12_list12_16;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class MakerClientThread extends Thread {
	private ActiveObject activeObject;
	private char fillchar;

	public MakerClientThread(String name, ActiveObject activeObject) {
		super(name);
		this.activeObject = activeObject;
		this.fillchar = name.charAt(0);
	}

	public void run() {
		try {
			for (int i = 0; true; i++) {
				Future<String> future = activeObject.makeString(i, fillchar);
				Thread.sleep(10);
				String value = future.get();
				System.out.println(Thread.currentThread().getName()
						+ ": value=" + value);
			}
		} catch (RejectedExecutionException e) {
			System.out.println(Thread.currentThread().getName() + ":" + e);
		} catch (CancellationException e) {
			System.out.println(Thread.currentThread().getName() + ":" + e);
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
