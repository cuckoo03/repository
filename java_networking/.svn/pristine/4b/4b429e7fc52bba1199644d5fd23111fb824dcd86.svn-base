package com.thread.ch7_list7_6;

import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main Begin");
		Host host = new Host(new ThreadFactory() {
			public Thread newThread(Runnable r) {
				return new Thread(r);
			}
		});
		host.request(10, 'A');
		host.request(10, 'B');
		System.out.println("Main End");
	}

}
