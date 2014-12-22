package com.thread.ch05_05;

import java.util.Random;

public class EaterThread implements Runnable {
	private final String name;
	private final Table table;
	private final Random random;

	public EaterThread(String name, Table table, int i) {
		this.name = name;
		this.table = table;
		this.random = new Random(i);
	}

	public void run() {
		while (true) {
			String cake = table.take();
		}
	}
}
