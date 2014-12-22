package com.thread.ch7_exam7_2;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main Begin");
		Host host = new Host();
		host.request(10, 'A');
		host.request(10, 'B');
		System.out.println("Main End");
	}

}
