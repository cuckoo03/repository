package com.thread.ch04_list4_1;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Data data = new Data("data.txt", "(empty)");
		new ChangerThread("ChangerThread", data).start();
		new ServerThread("ServerThread", data).start();
	}

}
