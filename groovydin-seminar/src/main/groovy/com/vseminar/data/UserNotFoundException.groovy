package com.vseminar.data

import com.vaadin.client.SuperDevMode;

import groovy.transform.TypeChecked;

@TypeChecked
class UserNotFoundException extends RuntimeException {
	UserNotFoundException() {
		super()
	}
	
	UserNotFoundException(final String message, final Throwable cause) {
		super(message, cause)
	}
	
	UserNotFoundException(final String message) {
		super(message)
	}
	
	UserNotFoundException(final Throwable cause) {
		super(cause)
	}
}
