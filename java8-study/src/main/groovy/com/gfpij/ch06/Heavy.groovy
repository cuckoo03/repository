package com.gfpij.ch06

import groovy.transform.TypeChecked

@TypeChecked
class Heavy {
	Heavy() {
		println "heavy created"
	}
	@Override
	def String toString() {
		return "quit heavy"
	}
}
