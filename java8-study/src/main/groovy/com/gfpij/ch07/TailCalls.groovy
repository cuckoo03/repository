package com.gfpij.ch07

import groovy.transform.TypeChecked

@TypeChecked
class TailCalls {
	def static <T> TailCall<T> call(TailCall<T> nextCall) {
		return nextCall
	}
	def static <T> TailCall<T> done(T value) {
		new TailCall<T>() {
			@Override
			def boolean isComplete() { 
				return true 
			}
			@Override
			public T result() { 
				return value 
			}
			@Override
			public TailCall<T> apply() {
				throw new Error("not implemented")
			}
		}
	}
}
