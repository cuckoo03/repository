package com.groovy.release.r1_5

import groovy.transform.TypeChecked;

@TypeChecked
class NamedParameterExam {
	static main(args) {
		def f = new Fund(p1: "p1", p2: "p2")
	}
}
class Fund {
	String p1
	String p2
}