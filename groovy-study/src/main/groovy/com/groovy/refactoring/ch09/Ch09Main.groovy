package com.groovy.refactoring.ch09

import groovy.transform.TypeChecked;

@TypeChecked
class Ch09Main {

	static main(args) {
		def logger = new Logger()
		logger.log("info 1")
		
		logger.start()
		logger.log("info 2")
		
		logger.start()
		logger.log("info 3")
		
		logger.stop()
		logger.log("info 4")
		
		logger.stop()
		logger.log("info 5")
	}

}
