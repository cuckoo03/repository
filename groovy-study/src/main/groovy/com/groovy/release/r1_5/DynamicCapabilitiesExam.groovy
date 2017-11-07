package com.groovy.release.r1_5

import groovy.transform.TypeChecked;

@TypeChecked
class DynamicCapabilitiesExam {

	static main(args) {
		def msg = "Hello!"
		println msg.metaClass
//		String.metaClass.up = { delegate -> delegate.toUpperCase() }
//		assert "HELLO!" == msg.up()
		
		msg.metaClass.methods.each {
			println it.name
		}
		println "---------------------"
		msg.metaClass.properties.each {
			println it.name
		}
	}
}
