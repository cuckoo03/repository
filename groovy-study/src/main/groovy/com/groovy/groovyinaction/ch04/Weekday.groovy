package com.groovy.groovyinaction.ch04

import groovy.transform.TypeChecked

class Weekday implements Comparable {
	static final List DAYS = ["sun", "mon", "tue", "wed", "thu", "fri", "sat"]

	private int index = 0

	Weekday(String day) {
		index = DAYS.indexOf(day)
	}

	Weekday next() {
		return new Weekday(DAYS[(index + 1) % DAYS.size()])
	}

	Weekday previous() {
		return new Weekday(DAYS[index - 1])
	}

	public int compareTo(Weekday other) {
		return this.index <=> other.index
	}

	@Override
	String toString() {
		DAYS[index]
	}
}
/*
def mon = new Weekday("mon")
def fri = new Weekday("fri")

def working = ''
for (day in mon..fri) {
	working += day.toString() + ' '
}
assert working == 'mon tue wed thu fri '
*/
