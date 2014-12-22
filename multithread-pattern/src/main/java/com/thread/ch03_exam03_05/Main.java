package com.thread.ch03_exam03_05;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.thread.ch03.Request;
import com.thread.ch03.RequestQueue;

public class Main {
	public static void main(String[] args) {
		RequestQueue input = new RequestQueue();
		RequestQueue output = new RequestQueue();
		ThreadFactory factory = Executors.defaultThreadFactory();
		
		try {
			input.putRequest(new Request("-1"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		factory.newThread(new TalkThread(input, output, "Bobby")).start();
		factory.newThread(new TalkThread(output, input, "Alice")).start();
	}
}
