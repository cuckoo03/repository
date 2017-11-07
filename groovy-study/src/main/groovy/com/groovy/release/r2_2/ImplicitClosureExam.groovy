package com.groovy.release.r2_2

import groovy.transform.TypeChecked;

@TypeChecked
class ImplicitClosureExam {
	static main(args) {
		def input = [1, 2, 3, 4, 5]
		def odd = filter(input) { int it -> it % 2 == 1 }
		assert odd == [1, 3, 5]

		def odd2 = filter(input, { (it as int) % 2 == 1} as Predicate)
		assert odd2 == [1, 3, 5]
	}
	static List filter(List list, Predicate pred) {
		(List) list.findAll { pred.test(it) }
	}
}
interface Predicate {
	boolean test(obj)
}
