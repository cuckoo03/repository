package com.thread.ch9_list9_6;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main Begin");
		Host host = new Host();
		Data data1 = host.request(10, 'A');
		Data data2 = host.request(20, 'B');
		Data data3 = host.request(30, 'C');

		System.out.println("Main other job begin");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main other job end");

		System.out.println("data1=" + data1.getContent());
		System.out.println("data2=" + data2.getContent());
		System.out.println("data3=" + data3.getContent());
		System.out.println("Main end");
	}

}
