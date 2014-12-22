package com.thread.ch8_exam8_2;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		Channel channel = new Channel(5);
		channel.startWorkers();
		ThreadFactory factory = Executors.defaultThreadFactory();
		factory.newThread(new ClientThread("A", channel)).start();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

}
