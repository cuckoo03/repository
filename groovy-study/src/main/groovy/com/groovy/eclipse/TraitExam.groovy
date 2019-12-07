package com.groovy.eclipse

import groovy.transform.TypeChecked

trait Greatable {
	def void greet() {
		println "hello"
	}
}
class Person implements Greatable {
}
@TypeChecked
class TraitExam {
	static void main(args) {
		def person = new Person()
		person.greet()
	}
}
