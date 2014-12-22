package com.thread.ch03_list3_6;

public class ClientThread implements Runnable {
	private RequestQueue queue;
	private String name;
	private int count;

	public ClientThread(RequestQueue queue, String string, int i) {
		this.queue = queue;
		this.name = string;
		this.count = i;
	}

	public void run() {
		Request request = null;
		for (int i = 0; i < count; i++) {
			request = new Request(Integer.toString(i));
			queue.putRequest(request);
			System.out.println(name + " put " + "[" + request.getName() + "]");
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
	}
}
