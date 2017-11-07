package com.groovy.release.r1_5

import groovy.transform.TypeChecked;

@TypeChecked
class ElivisOperatorExam {
	static main(args) {
		def name = "A"
		def display = name ? name : "unknown"
		println display

		def display2 = name ?: "unknown"
		println display2
	}
}
