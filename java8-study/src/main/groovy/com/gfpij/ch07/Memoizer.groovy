package com.gfpij.ch07

import groovy.transform.TypeChecked

import java.util.function.BiFunction
import java.util.function.Function

@TypeChecked
class Memoizer {
	def static <T, R> R callMemoized(
		BiFunction<Function<T, R>, T, R> function, T t) {
		def memoized = new Function<T, R>() {
			def Map<T, R> store = new HashMap<>()
			@Override
			public R apply(T input2) {
				return store.computeIfAbsent(
					input2, { key -> function.apply(this, key) })
			}
		}
		return memoized.apply(t)
	}
}
