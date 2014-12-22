package com.thread.chap01_2;

public class Log {
	public static void println(String s) {
		System.out.println(Thread.currentThread().getName() + " : " + s);
	}
}
