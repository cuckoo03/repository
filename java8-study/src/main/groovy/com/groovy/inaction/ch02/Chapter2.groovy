package com.groovy.inaction.ch02

import com.java.inaction.ch02.Apple
import com.java.inaction.ch02.AppleHeavyWeightPredicate;
import com.java.inaction.ch02.ApplePredicate;

import groovy.transform.TypeChecked;

@TypeChecked
class Chapter2 {
	static void printApple(List<Apple> apples, ApplePredicate predicate) {
		apples.each {
			if (predicate.test(it))
				println it.weight
		}
	}

	static main(args) {
		def List<Apple> apples = []
		apples += new Apple(1)
		apples += new Apple(156)
		apples += new Apple(161)
		
		printApple(apples, new AppleHeavyWeightPredicate())
	}
}
