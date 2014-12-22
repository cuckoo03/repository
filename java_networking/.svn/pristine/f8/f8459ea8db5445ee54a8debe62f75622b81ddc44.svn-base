package com.thread.ch7_list7_9;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
	public static void main(String[] args) {
		System.out.println("Main Begin");
		Host host = new Host(new Executor() {
			public void execute(Runnable command) {
				new Thread(command).start();
			}
		});
		host.request(10, 'A');
		host.request(10, 'B');
		System.out.println("Main End");
	}

}
