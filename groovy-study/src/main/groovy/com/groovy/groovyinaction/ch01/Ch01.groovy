package com.groovy.groovyinaction.ch01

import groovy.transform.TypeChecked;

@TypeChecked
class Ch01 {

	static main(args) {
		def person = new Person()
		println person.name
		println person.getName()
	}

}
class Person {
	private String name = "name" 
	def getName() {
		name
	}
}