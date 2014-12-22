package com.thread.ch05_05;

import java.util.Random;

public class MakerThread implements Runnable {
	private final String name;
	private final Table table;
	private final Random random;
	private static int count = 0;

	public MakerThread(String string, Table table, int i) {
		this.name = string;
		this.table = table;
		this.random = new Random(i);
	}

	public void run() {
		while (true) {
			String cake = "[" + nextId() + "] ";
			table.put(cake);
		}
	}

	public static int nextId() {
		return count++;
	}
}
