package com.thread.ch04_exam4_4_1;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class Main {
	public static void main(String[] args) {
		RequestQueue queue = new RequestQueue(5);
		ThreadFactory factory = Executors.defaultThreadFactory();
		
//		factory.newThread(new ClientThread(queue, "Client A", 1000000)).start();
		factory.newThread(new ClientThread(queue, "Client B", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server C", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server D", 1000000)).start();
	}

}
