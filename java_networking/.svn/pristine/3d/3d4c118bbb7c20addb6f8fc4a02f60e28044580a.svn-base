package com.thread.ch8_list8_1;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		Channel channel = new Channel(5);
		channel.startWorkers();
		ThreadFactory factory = Executors.defaultThreadFactory();
		factory.newThread(new ClientThread("A", channel)).start();
		factory.newThread(new ClientThread("B", channel)).start();
		factory.newThread(new ClientThread("C", channel)).start();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
