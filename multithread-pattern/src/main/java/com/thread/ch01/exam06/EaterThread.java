package com.thread.ch01.exam06;

import java.util.concurrent.Semaphore;

public class EaterThread extends Thread {
	private String name;
	private final Tool lefthand;
	private final Tool righthand;

	public EaterThread(String string, Tool spoon, Tool fork) {
		this.name = string;
		this.lefthand = spoon;
		this.righthand = fork;
	}

	public void run() {
		while (true) {
			try {
				eat();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void eat() throws InterruptedException {
		try {
			synchronized (lefthand) {
				System.out.println(name + " takes up " + lefthand + "(left).");
				synchronized (righthand) {
					System.out.println(name + " takes up " + righthand
							+ "(right).");
					System.out.println(name + "is eating now");
					System.out.println(name + " puts down" + righthand
							+ "(right).");
				}
				System.out.println(name + " puts down " + lefthand + "(left).");
				System.out.println("-------------");
			}
		} finally {
		}
	}

}
