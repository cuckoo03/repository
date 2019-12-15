package com.fpij.ch07;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Memoizer {
	static <T, R> R call(BiFunction<Function<T, R>, T, R> function, T t) {
		Function<T, R> memoized = new Function<T, R>() {
			Map<T, R> store = new HashMap<>();
			@Override
			public R apply(T input) {
				return store.computeIfAbsent(
					input, k -> function.apply(this, k));
			}
		};
		return memoized.apply(t);
	}
}
