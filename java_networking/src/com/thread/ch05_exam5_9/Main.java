package com.thread.ch05_exam5_9;

public class Main {
	public static void main(String[] args) {
		System.out.println("Start");
		try {
			WaitClass.method(3000);
		} catch (InterruptedException e) {
//			e.printStackTrace();
		}
		System.out.println("End");
	}

}
