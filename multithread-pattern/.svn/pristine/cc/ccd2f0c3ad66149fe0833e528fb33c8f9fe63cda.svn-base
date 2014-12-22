package com.thread.chap01_2;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		BoundedResource resource = new BoundedResource(2);
		ThreadFactory factory = Executors.defaultThreadFactory();
		
		for (int i = 0; i < 5; i++) {
			factory.newThread(new UserThread(resource)).start();
		}
	}

}
