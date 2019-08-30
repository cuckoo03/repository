package com.groovy.refactoring.ch10

import groovy.transform.TypeChecked;

@TypeChecked
class Ch10Main {

	static main(args) {
		def robot = new Robot("A")
		println robot
		
		robot.execute("forward right forward1")
		println robot
	}
}
