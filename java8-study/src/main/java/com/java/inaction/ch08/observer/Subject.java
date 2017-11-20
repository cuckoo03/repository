package com.java.inaction.ch08.observer;

public interface Subject {
	void register(Observer o);

	void notify(String t);
}
