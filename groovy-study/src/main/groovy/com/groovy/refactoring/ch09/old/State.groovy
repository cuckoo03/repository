package com.groovy.refactoring.ch09.old

import groovy.transform.TypeChecked;

@TypeChecked
abstract class State {
	abstract int getTypeCode()
}
