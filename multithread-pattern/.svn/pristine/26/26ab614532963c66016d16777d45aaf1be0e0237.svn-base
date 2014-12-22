package com.thread.ch05_01;

import java.util.Random;

public class MakerThread implements Runnable {
	private final Random random;
	private final Table table;
	private String name;
	private static int id = 0;

	public MakerThread(String name, Table table, long seed) {
		this.name = name;
		this.table = table;
		this.random = new Random(seed);
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(random.nextInt(1000));
				String cake = "[ Cake no. " + nextId() + " by " + name + "]";
				table.put(cake);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static synchronized int nextId() {
		return id++;
	}
}
