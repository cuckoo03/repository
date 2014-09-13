package org.concurrent.study.ch06;

import com.skta.gnm.common.concurrent.NoSleepThread;

public class NoSleepTest {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		NoSleepThread t1 = new MyNoSleepThread();
		t1.start();

		NoSleepThread t2 = new MyNoSleepThread();
		// t2.start();

		Thread.sleep(3000);

		t1.finish();
		// t2.finish();
		System.out.println("finish");
	}

}

class MyNoSleepThread extends NoSleepThread {
	private int count = 0;

	@Override
	public void task() throws Exception {
		System.out.println(count++);
	}
}