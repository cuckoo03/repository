package com.groovy.release.r1_6

import groovy.transform.Immutable;
import groovy.transform.TypeChecked;

@TypeChecked
class ImmutableExam {
	static main(args) {
		def d = new Date()
		def c1 = new Coordinates(lat:1L, lng:2L, since: d)
		def c2 = new Coordinates(lat:1L, lng:2L, since: d)
		assert c1 == c2
		println c1.hashCode()
		println c2.hashCode()
		
	}

}
@Immutable
class Coordinates {
	Double lat, lng
	Date since
}