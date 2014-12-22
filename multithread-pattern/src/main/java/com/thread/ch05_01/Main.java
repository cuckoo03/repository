package com.thread.ch05_01;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.thread.ch05_exam5_5.ClearThread;

public class Main {
	public static void main(String[] args) {
		Table table = new Table(3);
		ThreadFactory factory = Executors.defaultThreadFactory();
		factory.newThread(new MakerThread("MakerThread-1", table, 1)).start();
		factory.newThread(new EaterThread("EaterThread-1", table, 1)).start();
		factory.newThread(new ClearThread(table)).start();
	}

}
