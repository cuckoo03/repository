package com.thread.ch04_exam4_4_1;

public class ServerThread implements Runnable {
	private RequestQueue queue;
	private String name;
	private int count;

	public ServerThread(RequestQueue queue, String string, int i) {
		this.queue = queue;
		this.name = string;
		this.count = i;
	}

	public void run() {
		Request request = null;
		for (int i = 0; i < count; i++) {
			request = queue.getRequest();
			System.out.println(name + " get " + "[" + request.getName() + "]");

			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				
			}
		}
	}

}
