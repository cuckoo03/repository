package com.groovy.refactoring.ch09.old

import com.groovy.refactoring.ch09.Logger
import groovy.transform.TypeChecked;

@TypeChecked
class StateStopped extends State {
	@Override
	def int getTypeCode() {
		return Logger.STATE_LOGGING
	}
}
