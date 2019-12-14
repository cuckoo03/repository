package com.fpij.ch03;

public class Person {
	private final String name;
	private final int age;

	public Person(final String name, final int age) {
		this.name = name;
		this.age = age;
	}

	/**
	 * ageDiff
	 * @param other
	 * @return
	 */
	public int ageDiff(final Person other) {
		return age - other.age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	@Override
	public String toString() {
		return String.format("%s - %d", name, age);
	}
}
