package com.java.tjpl.ch06;

public class Ch06Main {

	public static void main(String[] args) {
		Suit s = Suit.A;
		System.out.println(Suit.valueOf("B"));
		System.out.println(Suit.values());
		System.out.println(s.ordinal());
	}
}

enum Suit {
	A("A"),
	B("B");

	private String name;

	Suit(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
