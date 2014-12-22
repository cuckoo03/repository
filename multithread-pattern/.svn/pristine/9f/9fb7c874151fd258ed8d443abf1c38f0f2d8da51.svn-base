package com.thread.ch04_exam4_4;

public class Main {
	public static void main(String[] args) {
		RequestQueue requestQueue = new RequestQueue();
		long timeout = 2000;
		new ClientThread(requestQueue, "alice", 3141592L, timeout).start();
		new ServerThread(requestQueue, "bobby", 3141592L, timeout).start();
	}
}
