package com.java.inaction.ch08;

public class IsAllowCase implements ValidationStrategy {
	@Override
	public boolean execute(String s) {
		return s.matches("[a-z]+");
	}
}
