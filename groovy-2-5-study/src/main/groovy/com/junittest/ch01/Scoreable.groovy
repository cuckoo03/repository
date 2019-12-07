package com.junittest.ch01

import groovy.transform.TypeChecked;

@TypeChecked
@FunctionalInterface
interface Scoreable {
	Integer getScore()
}
