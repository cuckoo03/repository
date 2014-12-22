package com.thread.ch05_01;

public class Table {
	private String[] buffer;
	private int tail;
	private int head;
	private int count;

	public Table(int count) {
		this.buffer = new String[count];
		this.head = 0;
		this.tail = 0;
		this.count = 0;
	}

	public synchronized void put(String cake) throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " puts " + cake);
		while (count >= buffer.length) {
			System.out.println(Thread.currentThread().getName()
					+ " before puts " + cake);
			wait();
			System.out.println(Thread.currentThread().getName()
					+ " after puts " + cake);
		}

		buffer[tail] = cake;
		tail = (tail + 1) % buffer.length;
		// tail = tail + 1;
		// if ((tail / buffer.length) == 0) {
		// tail = 0;
		// }
		count++;
		notify();
	}

	public synchronized String take() throws InterruptedException {
		while (count <= 0) {
			System.out.println(Thread.currentThread().getName()
					+ " before take");
			wait();
			System.out
					.println(Thread.currentThread().getName() + " after take");
		}
		String cake = buffer[head];
		head = (head + 1) % buffer.length;
		// head = head + 1;
		// if ((head / buffer.length) == 0) {
		// head = 0;
		// }
		count--;
		notify();
		System.out.println(Thread.currentThread().getName() + " take " + cake);

		return cake;
	}

	public synchronized void clear() {
		while (count > 0) {
			String cake = buffer[head];
			System.out.println(Thread.currentThread().getName() + " clear "
					+ cake);
			head = (head + 1) % buffer.length;
			count--;
		}

		head = 0;
		tail = 0;
		count = 0;
		notify();
	}
}
