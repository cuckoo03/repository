package com.java.inaction.ch08.observer;

public class ObserverB implements Observer {
	@Override
	public void notify(String t) {
		if (t.contains("b"))
			System.out.println("b observer");
	}
}
