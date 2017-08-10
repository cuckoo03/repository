package com.vseminar.data

import groovy.transform.TypeChecked

import com.vaadin.server.Page;
import com.vaadin.server.VaadinService
import com.vaadin.server.WrappedSession
import com.vseminar.data.model.User

@TypeChecked
class UserSession implements Serializable {
	public static final String SESSION_KEY = UserSession.getClass().canonicalName
	UserData userData
	
	UserSession() {
		this.userData = UserData.instance
	}
	
	static User getUser() {
		return (User) (getCurrentSession() as WrappedSession).getAttribute(SESSION_KEY)
	}
	
	static void setUser(User user) {
		if (user == null) {
			getCurrentSession().removeAttribute(SESSION_KEY)
		} else {
			getCurrentSession().setAttribute(SESSION_KEY, user)
		}
	}
	
	static boolean isSignedIn() {
		return getUser() != null
	}

	void signin(String email, String password) {
		def user = userData.findByEmailAndPassword(email, password)	
		if (user.id == null) {
			throw new UserNotFoundException("user not found")
		}
		
		setUser(user)
	}
	
	static void signout() {
		getCurrentSession().invalidate()
		Page.getCurrent().reload()
	}
	
	private static WrappedSession getCurrentSession() {
		def request = VaadinService.getCurrentRequest()
		if (request == null) {
			throw new IllegalStateException("no ruquest bound to current thread")
		}

		def session = request.getWrappedSession()
		if (session == null) {
			throw new IllegalStateException("no session bound to current thread")
		}
		
		return session
	}
}
