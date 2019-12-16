package com.gfpij.ch08

import groovy.transform.Immutable
import groovy.transform.TypeChecked

@TypeChecked
@Immutable
class StockInfo {
	def String ticker
	def BigDecimal price
	@Override
	def String toString() {
		return "ticker:$ticker, price:$price"
	}
}
