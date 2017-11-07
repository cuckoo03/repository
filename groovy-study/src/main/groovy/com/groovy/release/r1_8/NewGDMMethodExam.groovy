package com.groovy.release.r1_8

import groovy.transform.TypeChecked;

@TypeChecked
class NewGDMMethodExam {
	static main(args) {
		// count
		def isEven = { int it -> it % 2 == 0}
		assert [1, 2, 3].count(isEven) == 1

		// countBy
		assert [0:1, 1:2] == [1, 2, 3].countBy { it % 2 }
		
		// plus
		
		
		// equals for sets and maps
		
		//toSet
	}
}
