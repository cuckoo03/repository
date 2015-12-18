package com.ml.ciinaction.ch03

import groovy.transform.TypeChecked

@TypeChecked
class LogFontSizeComputationStrategy extends FontSizeComputationStrategyImpl {
	@Override
	protected double scaleCount(double count) {
		return Math.log10(count)
	}
}
