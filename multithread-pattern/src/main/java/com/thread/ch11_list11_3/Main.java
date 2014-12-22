package com.thread.ch11_list11_3;

public class Main {
	public static void main(String[] args) {
		new ClientThread("A").start();
		new ClientThread("B").start();
		new ClientThread("C").start();
	}

}
