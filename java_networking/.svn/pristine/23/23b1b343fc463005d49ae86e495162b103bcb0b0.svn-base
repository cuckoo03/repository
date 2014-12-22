package com.thread.ch8_exam8_6;

import java.util.Random;

public class ClientThread extends Thread {
	private final Channel channel;
	private static final Random random = new Random();
	private final String name;
	private volatile boolean terminated;

	public ClientThread(String name, Channel channel) {
		this.name = name;
		this.channel = channel;
	}

	public void run() {
		try {
			for (int i = 0; !terminated; i++) {
				Request request = new Request(name, i);
				channel.putRequest(request);
				Thread.sleep(random.nextInt(100));
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " run interrupt");
			e.printStackTrace();
		}
	}

	public void stopThread() {
		terminated = true;
		this.interrupt();
	}
}
