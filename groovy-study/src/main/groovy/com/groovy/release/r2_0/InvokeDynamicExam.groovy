package com.groovy.release.r2_0

import groovy.transform.TypeChecked

import org.codehaus.groovy.control.CompilerConfiguration

@TypeChecked
class InvokeDynamicExam {

	static main(args) {
		def config = new CompilerConfiguration()
		config.getOptimizationOptions().put("indy", true)
		def shell = new GroovyShell(config)
	}

}
