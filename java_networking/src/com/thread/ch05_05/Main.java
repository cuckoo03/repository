package com.thread.ch05_05;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		Table table = new Table(3);
		ThreadFactory factory = Executors.defaultThreadFactory();
		factory.newThread(new MakerThread("makerThread1", table, 1)).start();
		factory.newThread(new MakerThread("makerThread2", table, 1)).start();
		factory.newThread(new MakerThread("makerThread3", table, 1)).start();
		factory.newThread(new EaterThread("eaterThread1", table, 1)).start();
	}

}
