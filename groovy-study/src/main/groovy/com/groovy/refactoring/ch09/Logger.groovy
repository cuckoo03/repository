package com.groovy.refactoring.ch09

import groovy.transform.TypeChecked;

@TypeChecked
class Logger {
	enum State {
		STATE_STOPPED {
			@Override
			def void start() {
				println "start logginga"
			}
			@Override
			def void stop() {
			}
			@Override
			def void log(String info) {
				println "ingnoring:$info"
			}
		},
		STATE_LOGGING {
			@Override
			def void start() {
			}
			@Override
			def void stop() {
				println "stop logging"
			}
			@Override
			def void log(String info) {
				println "logging:$info"
			}
		}
		abstract void start()
		abstract void stop()
		abstract void log(String info)
	}
	
	static final int STATE_STOPPED = 0
	static final int STATE_LOGGING = 1
	private State state
	Logger() {
		setState(State.STATE_STOPPED)
	}
	def State getState() {
		return state
	}
	def void setState(State state) {
		state = state
	}
	def void start() {
		state.start()
		setState(State.STATE_LOGGING)
	}
	def void stop() {
		state.stop()
		setState(State.STATE_STOPPED)
	}
	def void log(String info) {
		state.log(info)
	}
}
