package com.agile.ch24;

public interface TimeSource {
	public void registerObserver(ClockObserver observer);
}
