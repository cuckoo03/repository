package com.agile.ch24;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TimeSourceImplementation {
	private List<ClockObserver> observers = new ArrayList<>();

	public void registerObserver(ClockObserver observer) {
		observers.add(observer);
	}

	public void notify(int hours, int minutes, int seconds) {
		Iterator<ClockObserver> i = observers.iterator();
		while (i.hasNext()) {
			ClockObserver observer = (ClockObserver) i.next();
			observer.update(hours, minutes, seconds);
		}
	}
}