package com.groovy.release.r1_8

import groovy.transform.TypeChecked
import groovy.util.logging.Log

import java.util.logging.Level

@TypeChecked
class LogExam {
	static main(args) {
		def c = new Car()
	}
}
@Log
class Car {
	Car() {
		if (log.isLoggable(Level.INFO)) 
			log.info "car"
	}
}
