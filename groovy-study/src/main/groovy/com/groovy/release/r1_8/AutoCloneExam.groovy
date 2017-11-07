package com.groovy.release.r1_8

import groovy.transform.AutoClone;
import groovy.transform.TypeChecked;

@TypeChecked
class AutoCloneExam {
	static main(args) {
		def p = new Person()
		p.first = "f"
		p.last = "l"
		p.since = new Date()
		
		def clone = p.clone()
		assert p.first == clone.first
		assert p.last == clone.last
		assert p.since == clone.since
		println p
		println clone
		println p.since
		println clone.since
	}
}
@AutoClone
class Person {
	String first, last
	List favItems
	Date since
}
