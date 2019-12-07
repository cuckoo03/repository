package com.junittest.ch01

import groovy.transform.TypeChecked;

@TypeChecked
class ScoreColection {
	private List<Scoreable> scores = new ArrayList<>()
	def void add(Scoreable scoreable) {
		scores.add(scoreable)
	}	
	def int arithmeticMean() {
		def total = scores.collectNested { Scoreable it -> it.getScore() }.sum() as int
		return (total / scores.size()).toInteger()
	}
}
