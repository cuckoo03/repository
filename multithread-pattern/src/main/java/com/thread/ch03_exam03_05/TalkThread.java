package com.thread.ch03_exam03_05;

import com.thread.ch03.Request;
import com.thread.ch03.RequestQueue;

public class TalkThread implements Runnable {
	private RequestQueue input;
	private RequestQueue output;
	private String name;

	public TalkThread(RequestQueue input, RequestQueue output, String name) {
		this.input = input;
		this.output = output;
		this.name = name;
	}

	public void run() {
		System.out.println("Begin");
		for (int i = 0; i < 2000; i++) {
			try {
				Request request1 = input.getRequest();
				System.out.println(name + " get " + request1);
				
				Request request2 = new Request(request1.getName() + "!");
				System.out.println(name + " put " + request2);
				output.putRequest(request2);
				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("End");
	}
}
