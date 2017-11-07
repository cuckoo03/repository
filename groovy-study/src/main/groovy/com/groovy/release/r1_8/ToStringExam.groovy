package com.groovy.release.r1_8

import groovy.transform.ToString;
import groovy.transform.TypeChecked;

@TypeChecked
class ToStringExam {
	static main(args) {
		def p = new Person3(name: "p3", age: 3)
		println p
	}
}
@ToString(includeNames = true, includeFields = true)
class Person3 {
	String name
	int age
	private z = 0
}
