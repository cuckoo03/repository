package com.java.inaction.ch05;

public class Transaction {
	private final Trader t;
	private final int y;
	private final int v;
	
	public Transaction(Trader t, int y, int v) {
		this.t = t;
		this.y = y;
		this.v = v;
	}

	public Trader getT() {
		return t;
	}

	public int getY() {
		return y;
	}

	public int getV() {
		return v;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(t);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(v);
		return sb.toString();
	}
}
