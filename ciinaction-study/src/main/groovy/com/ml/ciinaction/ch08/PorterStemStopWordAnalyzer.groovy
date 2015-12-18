package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.LowerCaseFilter
import org.apache.lucene.analysis.PorterStemFilter
import org.apache.lucene.analysis.StopFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.standard.StandardTokenizer

@TypeChecked
class PorterStemStopWordAnalyzer extends Analyzer {
	private static final String[] stopWords = ["and", "of"]

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		def tokenizer = new StandardTokenizer(reader)
		def lowerCaseFilter = new LowerCaseFilter(tokenizer)
		def stopFilter = new StopFilter(lowerCaseFilter, stopWords)
		def stemFilter = new PorterStemFilter(stopFilter)

		return stemFilter
	}
}
