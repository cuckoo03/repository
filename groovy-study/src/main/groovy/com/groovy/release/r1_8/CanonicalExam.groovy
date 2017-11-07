package com.groovy.release.r1_8

import groovy.transform.Canonical;
import groovy.transform.ToString;
import groovy.transform.TypeChecked;

@TypeChecked
class CanonicalExam {
	static main(args) {
		def p1 = new Person5(name: "p1", age: 1)
		def p2 = new Person5("p1", 1)
		p2.name = "p2"
		println p1.equals(p2)
		println p1
	}
}
@Canonical
@ToString(includeNames = true)
class Person5 {
	String name
	int age
}
