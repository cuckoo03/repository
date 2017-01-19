package com.java.impatient.ch01;

public class Greeter {
	public void greet() {
		System.out.println("hello");
	}
}
class ConcurrentGreeter extends Greeter {
	@Override
	public void greet() {
		Thread t = new Thread(super::greet);
		t.start();
	}
}
