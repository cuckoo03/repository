package com.thread.ch04_list4_1;

import java.io.IOException;

public class ServerThread extends Thread {
	private final Data data;

	public ServerThread(String name, Data data) {
		super(name);
		this.data = data;
	}

	public void run() {
		try {
			while (true) {
				data.save();
				Thread.sleep(1000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
