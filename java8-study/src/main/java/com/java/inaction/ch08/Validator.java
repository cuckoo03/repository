package com.java.inaction.ch08;

public class Validator {
	private final ValidationStrategy strategy;
	
	public Validator(ValidationStrategy s) {
		this.strategy = s;
	}
	
	public boolean validate(String s) {
		return strategy.execute(s);
	}
}
