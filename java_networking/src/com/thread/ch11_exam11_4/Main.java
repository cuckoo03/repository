package com.thread.ch11_exam11_4;

public class Main {
	public static void main(String[] args) {
		new ClientThread("A").start();
		new ClientThread("B").start();
		new ClientThread("C").start();
	}

}
