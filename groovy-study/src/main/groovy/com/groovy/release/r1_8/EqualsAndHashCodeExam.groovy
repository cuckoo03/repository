package com.groovy.release.r1_8

import groovy.transform.EqualsAndHashCode;
import groovy.transform.TypeChecked;

@TypeChecked
class EqualsAndHashCodeExam {
	static main(args) {
		def c1 = new Coord(x:1, y:2)
		def c2 = new Coord(x:1, y:2)
		assert c1 == c2
		assert c1.hashCode() == c2.hashCode()
	}
}
@EqualsAndHashCode
class Coord {
	int x, y
}
