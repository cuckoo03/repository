package com.groovy.release.r2_0

import javax.management.InstanceOfQueryExp;

import groovy.transform.TypeChecked;

@TypeChecked
class TypeInferenceExam {
	static main(args) {
		assert test("abc") == "ABC"
		assert test(123) == "246"
	}
	static String test(Object val) {
		if (val instanceof String) {
//			((String) val).toUpperCase()
			val.toUpperCase()
		} else if (val instanceof Number) {
//			((Number) val).intValue().multiply(2)
			val.intValue() * 2
		}
	}
}
