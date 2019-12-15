package com.gfpij.ch07

import groovy.transform.TypeChecked

@TypeChecked
class Ch07Main {
	static void main(args) {
		try {
//			println Factorial.factoralRec(1000)
			
//			Factorial.factorialTailRec(1, 2).invoke()
		} catch (StackOverflowError e) {
			println e
		}
		
		def priceValues = [2,1,1,2,2,1,8,9,15]
		def rodCutter = new RodCutterBasic(priceValues)
		println rodCutter.maxProfit(11)
		println rodCutter.maxProfitWithMomoize(11)
	}
}
