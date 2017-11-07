package com.groovy.release.r1_6

import groovy.transform.TypeChecked;

@TypeChecked
class LazyExam {
	static main(args) {
		def p = new Person()
		assert !(p.dump().contains("A"))
		
		assert p.pets.size() == 3
		assert p.dump().contains("C")
	}
}
class Person {
	@Lazy
	def List pets = ["A", "B", "C"]
}