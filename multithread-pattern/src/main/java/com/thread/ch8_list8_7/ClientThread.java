package com.thread.ch8_list8_7;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class ClientThread implements Runnable {
	private final ExecutorService channel;
	private static final Random random = new Random();
	private final String name;

	public ClientThread(String name, ExecutorService channel) {
		this.name = name;
		this.channel = channel;
	}

	public void run() {
		try {
			for (int i = 0; true; i++) {
				Request request = new Request(name, i);
				channel.execute(request);
				Thread.sleep(random.nextInt(1000));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RejectedExecutionException e) {
			System.out.println(name + ":" + e);
		}
	}
}
