package com.ml.ciinaction.ch03

import groovy.transform.TypeChecked

@TypeChecked
class LogFontSizeComputationStrategy extends FontSizeComputationStrategyImpl {
	public LogFontSizeComputationStrategy(int numSizes, String prefix) {
		super(numSizes, prefix)
	}
	
	@Override
	protected double scaleCount(double count) {
		return Math.log10(count)
	}
}
