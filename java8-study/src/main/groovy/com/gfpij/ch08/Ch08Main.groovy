package com.gfpij.ch08

import java.util.function.Predicate
import java.util.stream.Stream

import groovy.transform.TypeChecked

@TypeChecked
class Ch08Main {
	static void main(args) {
		def final BigDecimal HUNDRED = new BigDecimal("100")
		def symbols = ["AMD"] as List<String>
		symbols.stream()
		.filter({ s -> YahooFinance.getPrice(s).compareTo(HUNDRED) > 0 })
		.sorted()
		.forEach(System.out.&println)
		
		findHighPriced(symbols.stream())
	}
	def static void findHighPriced(final Stream<String> symbols) {
		def highPrice = symbols.map(StockUtil.&getPrice)
			.filter(StockUtil.&isPriceLessThan(1000) as Predicate)
			.reduce(StockUtil.&pickHigh)
			.get()
		println highPrice
	}
}
