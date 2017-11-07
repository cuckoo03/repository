package com.groovy.release.r1_8

import groovy.transform.TupleConstructor;
import groovy.transform.TypeChecked;

@TypeChecked
class TupleConstructorExam {
	static main(args) {
		def p1 = new Person4(name: "p1", age: 1, addr: "a1")
		def p2 = new Person4("p1", 1, "a1")
		assert p1.name == p2.name
		assert p1.age == p2.age
		assert p1.addr == p2.addr
	}
}
@TupleConstructor
class Person4 {
	String name
	int age
	String addr
}
