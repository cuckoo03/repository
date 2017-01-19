package com.java.impatient.ch01;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class Chapter01 {
	public static void main(String[] args) {
		Greeter g = new ConcurrentGreeter();
		g.greet();

		variableRange();

		defaultMethod();

	}

	static void defaultMethod() {
		Person p = new Student();
		System.out.println(p.getName());

		Student s = new Student();
		System.out.println(s.getName());
	}

	static void variableRange() {
		// variable validation range
		Path first1 = Paths.get("/usr/bin");
		Comparator<String> comp = (first, second) -> Integer.compare(
				first.length(), second.length());
	}
}

interface Person {
	default String getName() {
		return "person";
	}
}

interface Person2 {
	String getName();
}
class PersonClass {
	public String getName() {
		return "PersonClass";
	}
}
class Student implements Person, Named {
	public String getName() {
		return "Student";
	}
}

interface Named {
	default String getName() {
		return getClass().getName() + "_" + hashCode();
	}
}

interface Named2 {
	String getName();
}
