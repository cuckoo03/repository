package com.gfpij.ch07

import java.util.function.BiFunction
import java.util.function.Function

import groovy.transform.TypeChecked

@TypeChecked
class RodCutterBasic {
	private List<Integer> prices
	RodCutterBasic(List<Integer> pricesForLength) {
		this.prices = pricesForLength
	}
	def int maxProfit(int length) {
		def profit = (length <= prices.size()) ? prices.get(length - 1) : 0
		for (def i = 1; i < length; i++) {
			def priceWhenCut = maxProfit(i) + maxProfit(length - 1)
			if (profit < priceWhenCut) 
				profit = priceWhenCut
		}
		return profit
	}
	def int maxProfitWithMomoize(int rodLength) {
		def compute = { Function<Integer, Integer> func, Integer length ->
			def profit = (length <= prices.size()) ? prices.get(length - 1) : 0
			for (def i = 1; i < length; i++) {
				def priceWhenCut = func.apply(i) + func.apply(length - 1)
				if (profit < priceWhenCut)
					profit = priceWhenCut
			}
			return profit
		} as BiFunction<Function<Integer, Integer>, Integer, Integer>
		return Memoizer.callMemoized(compute, rodLength) 
	}
}
