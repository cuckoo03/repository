package com.groovy.inaction.ch03

import groovy.transform.TypeChecked

import java.util.function.BiFunction
import java.util.function.Function

import com.java.inaction.ch02.Apple

@TypeChecked
class Chapter03 {
	static main(args) {
		def c = { Apple a1, Apple a2 -> a1.compareTo(a2) } as Function<Apple, Apple, Integer>
		
		def str = Arrays.asList("a", "b")
		str.sort({a1, a2 -> a1.compareTo(a2)})
	
		def c5 = {Integer i -> new Apple(i)} as Function
		BiFunction<String, Integer, Apple> c6 = {
			String s, Integer i ->
			new Apple(s, i)
		} as BiFunction

		def c7 = c6.apply("green", 1)
		println c7.weight
		
		// concat comparator
		def c8 = Comparator.comparing({Apple a -> a.weight})
		def c9 = Comparator.comparing({Apple a -> a.weight} as Function)
		c8.reversed().thenComparing(c9)
		
	}
}
