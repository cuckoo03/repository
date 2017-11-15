package com.groovy.inaction.ch03

import groovy.transform.TypeChecked

import java.util.function.BiFunction
import java.util.function.Function

import com.java.inaction.ch02.Apple

@TypeChecked
class Chapter03 {
	static main(args) {
		def c = { Apple a1, Apple a2 -> a1.compareTo(a2) }
		
		def str = Arrays.asList("a", "b")
		str.sort({a1, a2 -> a1.compareTo(a2)})
	
		def c5 = {Integer i -> new Apple(i)} as Function
		println c5
		def c6 = {String s, Integer i -> new Apple(s, i)} as BiFunction
		println c6
	}
}
