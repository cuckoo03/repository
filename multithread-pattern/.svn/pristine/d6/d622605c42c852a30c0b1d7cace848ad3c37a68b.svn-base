package com.thread.ch9_exam9_4_1;

import java.util.concurrent.ExecutionException;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main Begin");
		Host host = new Host();
		Data data1 = host.request(-1, 'A');

		try {
			System.out.println("data1=" + data1.getContent());
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("Main end");
	}

}
