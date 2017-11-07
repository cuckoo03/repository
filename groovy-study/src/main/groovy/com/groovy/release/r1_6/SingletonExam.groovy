package com.groovy.release.r1_6

import java.util.ServiceLoader.LazyIterator;

import groovy.transform.TypeChecked;

@TypeChecked
class SingletonExam {
	static main(args) {
		def t1 = T.instance
		def t2 = T.instance
		assert t1 == t2
	}
}
@Singleton(lazy = true)
class T {}
