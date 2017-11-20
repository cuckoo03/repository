package com.java.inaction.ch08.observer;

@FunctionalInterface
public interface Observer {
	void notify(String t);
}
