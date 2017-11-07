package com.groovy.release.r2_3

import groovy.transform.TypeChecked;
import groovy.transform.builder.Builder;

@TypeChecked
class BuilderExam {

	static main(args) {
		def p = Person.builder().firstName("f").lastName("l").age(1)
		println p.firstName
	}
}
@Builder
class Person {
	String firstName
	String lastName
	int age
}
