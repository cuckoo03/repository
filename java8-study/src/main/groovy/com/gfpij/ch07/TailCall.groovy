package com.gfpij.ch07

import java.util.stream.Stream

import groovy.transform.TypeChecked

@TypeChecked
@FunctionalInterface
trait TailCall<T> {
	def abstract TailCall<T> apply()
	def boolean isComplete() {
		return false
	}
	def T result() {
		throw new Error("not implemented")
	}
	def T invoke() {
		return Stream.iterate(this, TailCall.&apply)
			.filter(TailCall.&isComplete)
			.findFirst().get().result()
	}
}
