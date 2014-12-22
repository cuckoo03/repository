package com.thread.ch03_list3_6;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		RequestQueue queue = new RequestQueue();
		ThreadFactory factory = Executors.defaultThreadFactory();
		
		factory.newThread(new ClientThread(queue, "Client A", 1000000)).start();
		factory.newThread(new ClientThread(queue, "Client B", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server B", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server C", 1000000)).start();
	}

}
