package com.thread.ch01.exam05;

public class SecurityGate {
	private int counter = 0;

	public synchronized void enter() {
		int currentCounter = counter;
		counter = currentCounter + 1;
		// counter++;
	}

	public synchronized void exit() {
		int currentCounter = counter;
		Thread.yield();
		counter = currentCounter - 1;
		// counter++;
	}

	public synchronized int getCounter() {
		return counter;
	}
}
