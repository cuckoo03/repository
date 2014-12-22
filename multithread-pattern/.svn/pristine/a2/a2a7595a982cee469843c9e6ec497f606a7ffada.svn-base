package com.thread.ch05_exam5_9;

public class WaitClass {
	public static void method(int count) throws InterruptedException {
		if (count != 0) {
			Object obj = new Object();
			synchronized (obj) {
				obj.wait(count);
			}
		}
	}
}
