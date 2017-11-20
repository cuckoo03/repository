package com.java.inaction.ch08;

public class IsNumeric implements ValidationStrategy {
	public boolean execute(String s) {
		return s.matches("\\d+");
	}

}
