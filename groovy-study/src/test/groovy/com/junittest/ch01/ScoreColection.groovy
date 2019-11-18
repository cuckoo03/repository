package com.junittest.ch01

import com.groovy.groovyinaction.ch07.Summer;

import groovy.transform.TypeChecked;

@TypeChecked
class ScoreColection {
	private List<Scoreable> scores = []
	def void add(Scoreable scoreable) {
		scores.add(scoreable)
	}	
	def int arithmeticMean() {
		def total = scores.collectAll { Scoreable it -> it.getScore() }.sum() as int
		return (total / scores.size()).toInteger()
	}
}
