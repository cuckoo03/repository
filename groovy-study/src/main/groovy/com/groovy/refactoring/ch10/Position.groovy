package com.groovy.refactoring.ch10

import groovy.transform.TypeChecked;

@TypeChecked
class Position {
	int x
	int y 
	Position(int x, int y) {
		this.x = x
		this.y = y
	}
	def void relativeMove(int dx, int dy) {
		x += dx	
		y += dy
	}
}
