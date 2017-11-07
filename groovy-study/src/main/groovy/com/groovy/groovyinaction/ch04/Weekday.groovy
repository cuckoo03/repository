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

	@Override
	public int compareTo(Object other) {
		return this.index <=> (other as Weekday).index
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
