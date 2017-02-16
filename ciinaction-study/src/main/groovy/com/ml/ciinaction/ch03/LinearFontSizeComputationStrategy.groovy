package com.ml.ciinaction.ch03

import groovy.transform.TypeChecked

@TypeChecked
class LinearFontSizeComputationStrategy extends FontSizeComputationStrategyImpl {
	public LinearFontSizeComputationStrategy(int numSizes, String prefix) {
		super(numSizes, prefix)
	}

	@Override
	protected double scaleCount(double count) {
		return count
	}
}
