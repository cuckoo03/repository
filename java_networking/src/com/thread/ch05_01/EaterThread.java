package com.thread.ch05_01;

import java.util.Random;

public class EaterThread implements Runnable {
	private Random random;
	private Table table;
	private String name;

	public EaterThread(String name, Table table, long seed) {
		this.name = name;
		this.table = table;
		this.random = new Random(seed);
	}

	public void run() {
		try {
			while (true) {
				table.take();
				Thread.sleep(random.nextInt(1000));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
