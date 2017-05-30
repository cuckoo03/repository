package com.java.inaction.ch03;

@FunctionalInterface
public interface Function<T, R> {
	R apply(T t);
}
