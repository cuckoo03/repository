package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.junit.Test

@TypeChecked
class SynonymAnalyzerViewer {
	@Test
	public void test() {
		AnalyzerUtils.displayTokensWithPositions(
				new SynonymAnalyzer(new MockSynonymEngine()),
				"The quick brown fox jumps over the lazy dogs")
	}
}
