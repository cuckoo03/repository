package com.groovy.release.r1_6

import groovy.transform.TypeChecked;

@TypeChecked
class MultiAssignExam {
	static main(args) {
		def (a, b) = [1, 2]
		assert a == 1
		assert b == 2
	}
}
