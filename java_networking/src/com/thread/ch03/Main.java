package com.thread.ch03;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RequestQueue requestQueue = new RequestQueue();
		new ClientThread(requestQueue, "alice", 3141592L).start();
		new ServerThread(requestQueue, "bobby", 3141592L).start();
		new ServerThread(requestQueue, "chali", 3141592L).start();
	}

}
