package com.java.inaction.ch09;

public interface A {
	default void hello() {
		System.out.println("hello A");
	}
}
