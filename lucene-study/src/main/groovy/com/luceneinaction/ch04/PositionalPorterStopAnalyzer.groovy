package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.LowerCaseTokenizer
import org.apache.lucene.analysis.PorterStemFilter
import org.apache.lucene.analysis.StopAnalyzer
import org.apache.lucene.analysis.StopFilter
import org.apache.lucene.analysis.TokenStream

//@TypeChecked
class PositionalPorterStopAnalyzer extends Analyzer {
	private Set stopWords

	public PositionalPorterStopAnalyzer() {
		this(StopAnalyzer.ENGLISH_STOP_WORDS)
	}

	public PositionalPorterStopAnalyzer(String[] stopList) {
		this.stopWords = StopFilter.makeStopSet(stopList)
	}

	@Override
	public TokenStream tokenStream(String fileName, Reader reader) {
		return new PorterStemFilter(
				new PositionalStopFilter(
				new LowerCaseTokenizer(reader), stopWords))
	}
}
