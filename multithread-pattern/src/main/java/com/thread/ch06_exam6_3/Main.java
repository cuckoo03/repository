package com.thread.ch06_exam6_3;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class Main {
	public static void main(String[] args) {
		Data data = new Data(10);
		ThreadFactory factory = Executors.defaultThreadFactory();
		factory.newThread(new ReaderThread(data)).start();
		factory.newThread(new ReaderThread(data)).start();
		factory.newThread(new ReaderThread(data)).start();
		factory.newThread(new ReaderThread(data)).start();
		factory.newThread(new ReaderThread(data)).start();
		factory.newThread(new WriterThread(data, "ABCDEFG")).start();
	}
}
