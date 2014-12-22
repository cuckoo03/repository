package com.thread.ch03_exam03_06;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.thread.ch03.Request;
import com.thread.ch03.RequestQueue;
import com.thread.ch03_exam03_05.TalkThread;

public class Main {
	public static void main(String[] args) {
		RequestQueue input = new RequestQueue();
		RequestQueue output = new RequestQueue();
		ThreadFactory factory = Executors.defaultThreadFactory();

		try {
			input.putRequest(new Request("-1"));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Thread t1 = factory.newThread(new TalkThread(input, output, "Bobby"));
		Thread t2 = factory.newThread(new TalkThread(output, input, "Alice"));

		t1.start();
		t2.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("calling interrupt");
		t1.interrupt();
		t2.interrupt();
	}
}
