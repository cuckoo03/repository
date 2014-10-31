package com.pecktpub.java7.concurrency.chap6.priority;

public class Event implements Comparable<Event>{
	private int thread;
	private int priority;
	
	public Event(int thread, int priority) {
		this.thread = thread;
		this.priority = priority;
	}
	@Override
	public int compareTo(Event e) {
		// TODO Auto-generated method stub
		if (this.priority > e.getPriority()) {
			return -1;
		} else if (this.priority < e.getPriority()) {
			return 1;
		}
		return 0;
	}
	public int getThread() {
		return thread;
	}
	public int getPriority() {
		return priority;
	}

}
