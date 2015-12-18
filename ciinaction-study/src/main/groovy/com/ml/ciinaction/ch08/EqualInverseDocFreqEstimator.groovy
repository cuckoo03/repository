package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

@TypeChecked
class EqualInverseDocFreqEstimator implements InverseDocFreqEstimator {
	@Override
	public double estimateInverseDocFreq(Tag tag) {
		return 1.0
	}
}
