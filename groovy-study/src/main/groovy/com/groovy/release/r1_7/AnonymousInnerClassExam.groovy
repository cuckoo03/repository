package com.groovy.release.r1_7

import groovy.transform.TypeChecked;

@TypeChecked
class AnonymousInnerClassExam {
	static main(args) {
		new A.B()
	}
}
class A {
	static class B {}
}
