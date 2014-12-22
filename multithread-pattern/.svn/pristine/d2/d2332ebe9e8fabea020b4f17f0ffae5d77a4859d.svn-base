package com.thread.ch12_list12_16;

import java.util.concurrent.CancellationException;
import java.util.concurrent.RejectedExecutionException;

public class DisplayClientThread extends Thread {
	private ActiveObject activeObject;

	public DisplayClientThread(String name, ActiveObject activeObject) {
		super(name);
		this.activeObject = activeObject;
	}

	public void run() {
		try {
			for (int i = 0; true; i++) {
				String string = Thread.currentThread().getName() + " " + i;
				activeObject.displayString(string);
				Thread.sleep(200);
			}
		} catch (RejectedExecutionException e) {
			System.out.println(Thread.currentThread().getName() + ":" + e);
		} catch (CancellationException e) {
			System.out.println(Thread.currentThread().getName() + ":" + e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
