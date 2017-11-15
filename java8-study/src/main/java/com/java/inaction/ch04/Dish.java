package com.java.inaction.ch04;

public class Dish {
	private final String n;
	private final boolean ve;
	private final int cal;
	private final Type t;

	public Dish(String n, boolean ve, int cal, Type t) {
		this.n = n;
		this.ve = ve;
		this.cal = cal;
		this.t = t;
	}

	/**
	 * getName
	 * @return
	 */
	public String getN() {
		return n;
	}

	public boolean isVe() {
		return ve;
	}

	public int getCal() {
		return cal;
	}

	public Type getType() {
		return t;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(n);
		sb.append(", ");
		sb.append(ve);
		sb.append(", ");
		sb.append(cal);
		sb.append(", ");
		sb.append(t);
		return sb.toString();
	}
	
	public enum Type {
		MEAT, FISH, OTHER
	}
}
