package com.java.inaction.ch02;

@FunctionalInterface
public interface Predicate<T> {
	boolean test(T t);
}
