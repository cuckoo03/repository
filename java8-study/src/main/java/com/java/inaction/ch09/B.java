package com.java.inaction.ch09;

public interface B {
	default void hello() {
		System.out.println("hello B");
	}
}
