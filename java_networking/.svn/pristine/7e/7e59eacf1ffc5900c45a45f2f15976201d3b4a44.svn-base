package com.thread.ch05_05;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Table extends LinkedBlockingQueue<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Table(int arg0) {
		super(arg0);
	}

	public void put(String cake) {
		try {
			System.out.println(Thread.currentThread().getName() + " wait for put");
			super.put(cake);
			System.out.println(Thread.currentThread().getName() + " puts "
					+ cake);
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String take() {
		String cake = null;
		try {
			System.out.println(Thread.currentThread().getName() + " wait for take");
			cake = super.take();
			System.out.println(Thread.currentThread().getName() + " takes "
					+ cake);
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return cake;
	}
}
