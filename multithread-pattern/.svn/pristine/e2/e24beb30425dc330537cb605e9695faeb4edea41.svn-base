package com.thread.ch02.exam02_15;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class CrackerThread implements Runnable {
	private final MutablePerson mutable;

	public CrackerThread(MutablePerson mutable) {
		this.mutable = mutable;
	}

	public void run() {
		while (true) {
			ImmutablePerson imutable = new ImmutablePerson(mutable);
			if (!imutable.getName().equals(imutable.getAddress())) {
				System.out.println("current thread: "
						+ Thread.currentThread().getName() + " " + "broken: "
						+ mutable.getName() + ", " + mutable.getAddress());
			}
		}
	}
}

public class Main1 {
	public static void main(String[] args) {
		MutablePerson mutable = new MutablePerson("start", "start");
		ThreadFactory factory = Executors.defaultThreadFactory();

		factory.newThread(new CrackerThread(mutable)).start();

		for (int i = 0; true; i++) {
			mutable.setPerson("" + i, "" + i);
		}
	}
}
