package com.groovy.release.r1_7

import groovy.transform.TypeChecked;

@TypeChecked
class NestedClassExam {
	static main(args) {
		def y = new Y()
		def x = y.foo()
	}
}
public class Y {
	public class X {}
	public X foo() {
		return new X(this)
	}
	public static X createX(Y y) {
		return new X(y)
	}
}
