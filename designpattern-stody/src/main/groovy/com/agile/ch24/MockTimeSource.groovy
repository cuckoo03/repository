package com.agile.ch24

import groovy.transform.TypeChecked;

@TypeChecked
class MockTimeSource implements TimeSource {
	TimeSourceImplementation timeSource = new TimeSourceImplementation()

	public void setTime(int hours, int minutes, int seconds) {
		timeSource.notify(hours, minutes, seconds)
	}

	@Override
	public void registerObserver(ClockObserver observer) {
		timeSource.registerObserver(observer)
	}
}
