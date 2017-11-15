package com.java.inaction.ch05;

public class Trader {
	private final String n;
	private final String c;
	
	public Trader(String n, String c) {
		this.n = n;
		this.c = c;
	}

	public String getN() {
		return n;
	}

	public String getC() {
		return c;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(n);
		sb.append(", ");
		sb.append(c);
		return sb.toString();
	}
}
