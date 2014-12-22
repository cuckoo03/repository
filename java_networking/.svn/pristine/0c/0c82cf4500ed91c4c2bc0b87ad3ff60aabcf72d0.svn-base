package com.thread.ch05_exam5_8;

import com.thread.ch05_01.Table;

public class LazyThread implements Runnable {
	private String name;
	private Table table;

	public LazyThread(String name, Table table) {
		this.name = name;
		this.table = table;
	}

	public void run() {
		while (true) {
			try {
				synchronized (table) {
					table.wait();
				}
				System.out.println(name + " is notified");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
