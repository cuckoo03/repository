package com.gfpij.ch07

import groovy.transform.TypeChecked
import java.util.function.Consumer
import java.util.function.Function

@TypeChecked
class Factorial {
	def static int factoralRec(int number) {
		if (number == 1)
			return number
		else
			number * factoralRec(number - 1)
	} 
	def static TailCall<Integer> factorialTailRec(int factorial, int number) {
		if (number == 1)
			return TailCalls.done(factorial)
		else
			return TailCalls.call(
				{ factorialTailRec(factorial * number, number - 1) } as TailCall
			)
	}
}
