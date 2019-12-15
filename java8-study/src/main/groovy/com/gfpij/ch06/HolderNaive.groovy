package com.gfpij.ch06

import groovy.transform.TypeChecked

@TypeChecked
class HolderNaive {
	private Heavy heavy
	HolderNaive() {
		println "holder created"
	}
	def synchronized Heavy getHeavy() {
		if (heavy == null)
			heavy = new Heavy()
		return heavy
	}
}
