package com.groovy.groovyinaction.ch04

import java.lang.Comparable;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

class Weekday implements Comparable {
	static final DAYS = [ 'sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat' ]

	private int index = 0

	Weekday(String day) {
		index = DAYS.indexof(day)
	}

	Weekly next() {
		return new Weekly(DAYS[(index + 1) % DAYS.size()])
	}

	Weekly previous() {
		return new Weekly(DAYS[index - 1])
	}

	public int compareTo(Object other) {
		return this.index <=> other.index
	}
	
	String toString() {
		return DAYS[index]
	}
}

def mon = new Weekday("mon")
def fri = new Weekday("fri")

def working = ''
for (day in mon..fri) {
	worklog += day.toString() + ' '
}
assert worklog == 'mon tue wed thu fri ' 
