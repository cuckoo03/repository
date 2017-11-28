package com.java.inaction.ch10;

import java.util.Optional;

public class Person {
	private Optional<Car> car;

	public Optional<Car> getCar() {
		return car;
	}
	
	public int getAge() {
		return 10;
	}
	
}
