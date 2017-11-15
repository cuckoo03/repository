package com.groovy.optional

import groovy.transform.TypeChecked;

@TypeChecked
class OptionalMain {
	static main(args) {
		def gender = Optional.of("male")
		def answer1 = "Yes"
		def answer2 = null
		
		println gender
		println gender.get()
		println gender.empty()
		println ""
		println Optional.ofNullable(answer1)
		println Optional.ofNullable(answer2)
		println Optional.of(answer2)
	}
}