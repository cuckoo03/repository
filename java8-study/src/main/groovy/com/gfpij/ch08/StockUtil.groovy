package com.gfpij.ch08

import java.util.function.Predicate

import groovy.transform.TypeChecked

@TypeChecked
class StockUtil {
	def static StockInfo getPrice(final String ticker) {
		return new StockInfo(ticker:ticker, price:YahooFinance.getPrice(ticker))
	}
	def static Predicate<StockInfo> isPriceLessThan(final int price) {
		return { StockInfo stockInfo -> 
			stockInfo.price.compareTo(price) < 0 } as Predicate
	}
	def static StockInfo pickHigh(final StockInfo stock1, final StockInfo stock2) {
		return stock1.price.compareTo(stock2.price) > 0 ? stock1 : stock2 
	}
}
