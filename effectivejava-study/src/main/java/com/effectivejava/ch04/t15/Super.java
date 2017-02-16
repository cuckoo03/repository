package com.effectivejava.ch04.t15;

import java.util.Date;

public class Super {
	public Super() {
		m();
	}

	public void m() {

	}

	public static void main(String[] args) {
		Sub s = new Sub();
		s.m();
	}
}

final class Sub extends Super {
	private final Date date;

	Sub() {
		date = new Date();
	}

	public void m() {
		System.out.println(date);
	}
}
