package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.junit.Assert
import org.junit.Test

/**
 * 
 * @author cuckoo03
 *
 */
@TypeChecked
class StopAnalyzer2Test {
	@Test
	public void testStopAnalyzer2() {
		def tokens = AnalyzerUtils.tokensFromAnalysis(
				new StopAnalyzer2(), "The quick brown fox")

		AnalyzerUtils.assertTokensEqual(tokens,
				["quick", "brown", "fox"] as String[])
	}
}
