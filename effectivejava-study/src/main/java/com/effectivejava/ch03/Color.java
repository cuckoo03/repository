package com.effectivejava.ch03;

public class Color {
	@SuppressWarnings("unused")
	private String name;

	private Color(String name) {
		this.name = name;
	}

	public static final Color RED = new Color("red");
	public static final Color BLUE = new Color("red");
	public static final Color GREEN = new Color("red");

}
