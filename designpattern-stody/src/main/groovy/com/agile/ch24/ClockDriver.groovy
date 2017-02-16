package com.agile.ch24

import groovy.transform.TypeChecked;

@TypeChecked
class ClockDriver implements ClockObserver {
	private ClockObserver sink
	
	public ClockDriver(TimeSourceImplementation source, ClockObserver sink) {
		source.registerObserver(this)
		this.sink = sink
	}
	
	@Override
	public void update(int hours, int minutes, int seconds) {
//		sink.setTime(hours, minutes, seconds)
	}
}
