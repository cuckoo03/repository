package com.java.inaction.ch08.observer;

import java.util.ArrayList;
import java.util.List;

public class ConcreteSubject implements Subject {
	private final List<Observer> observers = new ArrayList<>();

	@Override
	public void register(Observer o) {
		this.observers.add(o);
	}

	@Override
	public void notify(String t) {
		observers.forEach(o -> o.notify(t));
	}
}
