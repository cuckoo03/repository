package com.thread.ch8_list8_1;

import java.util.Random;

public class ClientThread implements Runnable {
	private final Channel channel;
	private static final Random random = new Random();
	private final String name;

	public ClientThread(String name, Channel channel) {
		this.name = name;
		this.channel = channel;
	}

	public void run() {
//		try {
			for (int i = 0; true; i++) {
				Request request = new Request(name, i);
				channel.putRequest(request);
//				Thread.sleep(random.nextInt(100));
			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
}
