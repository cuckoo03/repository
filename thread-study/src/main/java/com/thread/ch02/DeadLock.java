package com.thread.ch02;

public class DeadLock {
	static class Friend {
		private final String name;

		public Friend(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public synchronized void bow(Friend bower) {
			System.out.println(this.name + "," + bower.getName());
			bower.bowBack(this);
		}

		private synchronized void bowBack(Friend bower) {
			System.out.println(this.name + "," + bower.getName());
		}
	}

	public static void main(String[] args) {
		final Friend a = new Friend("A");
		final Friend b = new Friend("B");
		new Thread(() -> a.bow(b)).start();
		new Thread(() -> b.bow(a)).start();
	}
}
