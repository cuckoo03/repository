package com.java.inaction.ch09;

public class C implements B, A {
	public void hello() {
		A.super.hello();
	}
	
	public static void main(String[] args) {
		new C().hello();
	}
}
