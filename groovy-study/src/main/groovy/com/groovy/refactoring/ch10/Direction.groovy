package com.groovy.refactoring.ch10

import groovy.transform.TypeChecked;

@TypeChecked
class Direction {
	int x
	int y
	Direction(int x, int y) {
		this.x = x
		this.y = y
	}
	def void setDirection(int x, int y) {
		this.x = x
		this.y = y
	}
}
