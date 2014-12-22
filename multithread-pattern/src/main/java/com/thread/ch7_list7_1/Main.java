package com.thread.ch7_list7_1;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main Begin");
		Host host = new Host();
		host.request(10, 'A');
		host.request(10, 'B');
		System.out.println("Main End");
	}

}
