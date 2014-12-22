package com.ch16.events;

import java.util.Map;

public class Job {
	private int eventType;
	private Map<String, Object> session = null;

	public Job(int eventType, Map<String, Object> session) {
		this.eventType = eventType;
		this.session = session;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

}
