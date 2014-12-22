package com.thread.ch05_exam5_6;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.thread.ch05_01.EaterThread;
import com.thread.ch05_01.MakerThread;
import com.thread.ch05_01.Table;
import com.thread.ch05_exam5_5.ClearThread;

public class Main {
	public static void main(String[] args) {
		System.out.println("start");

		Table table = new Table(3);
		ThreadFactory factory = Executors.defaultThreadFactory();
		Thread[] threads = new Thread[3];
		threads[0] = factory.newThread(new MakerThread("MakerThread-1", table,
				1));
		threads[1] = factory.newThread(new EaterThread("EaterThread-1", table,
				1));
		threads[2] = factory.newThread(new ClearThread(table));

		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("end");
		for (int i = 0; i < threads.length; i++) {
			threads[i].interrupt();
		}
	}
}
