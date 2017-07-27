package com.vseminar.data

import groovy.transform.TypeChecked

import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedSession

@TypeChecked
class UserSession implements Serializable {
	private static WrappedSession getCurrentSession() {
		def request = VaadinService.getCurrentRequest()
		if (request == null) {
			throw new IllegalStateException("no ruquest bound to current thread")
		}
		def session = request.getWrappedSession()
		if (session == null) {
			throw new IllegalStateException("no session bound to current thread")
		}
		
		session
	}
}
