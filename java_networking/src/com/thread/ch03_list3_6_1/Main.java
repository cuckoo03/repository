package com.thread.ch03_list3_6_1;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 카운터가 증가하면서 해당 값을 큐에 put, get하는 프로그램
 * @author cuckoo03
 *
 */
public class Main {
	public static void main(String[] args) {
		RequestQueue queue = new RequestQueue();
		ThreadFactory factory = Executors.defaultThreadFactory();
		
		factory.newThread(new ClientThread(queue, "Client A", 1000000)).start();
		factory.newThread(new ClientThread(queue, "Client B", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server C", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server D", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server E", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server F", 1000000)).start();
		factory.newThread(new ServerThread(queue, "Server G", 1000000)).start();
	}

}
