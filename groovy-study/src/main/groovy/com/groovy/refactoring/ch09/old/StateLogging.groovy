package com.groovy.refactoring.ch09.old

import com.groovy.refactoring.ch09.Logger
import groovy.transform.TypeChecked;

@TypeChecked
class StateLogging extends State {
	@Override
	int getTypeCode() {
		return Logger.STATE_STOPPED
	}
}
