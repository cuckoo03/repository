package com.java.inaction.ch08.observer;

public class ObserverA implements Observer {
	@Override
	public void notify(String t) {
		if (t.contains("a"))
			System.out.println("a observer");
	}
}
