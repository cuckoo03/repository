package com.java.inaction.ch03;

@FunctionalInterface
public interface Consumer<T> {
	void accept(T t);
}
