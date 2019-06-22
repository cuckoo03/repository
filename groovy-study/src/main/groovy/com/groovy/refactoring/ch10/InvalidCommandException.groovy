package com.groovy.refactoring.ch10

import groovy.transform.TypeChecked;

@TypeChecked
class InvalidCommandException extends Exception {
	InvalidCommandException(String name) {
		super(name)
	}
	InvalidCommandException() {}
}
