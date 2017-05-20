package com.groovy.errortest

import groovy.transform.TypeChecked

@TypeChecked
class A {
	public <T> T f(T value, int param = 0) {
		value
	}

	void run() {
		def s = f('42')
		s.length()
	}
}