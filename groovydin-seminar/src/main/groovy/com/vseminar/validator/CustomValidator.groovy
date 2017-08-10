package com.vseminar.validator

import groovy.transform.TypeChecked

import com.vaadin.data.Validator
import com.vaadin.data.Validator.InvalidValueException

@TypeChecked
class CustomValidator implements Validator {
	@Override
	void validate(Object value) throws InvalidValueException {
		if (!(value instanceof String && ((String) value) == "hello")) {
			throw new InvalidValueException("impolite")
		}
	}
}
