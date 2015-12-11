package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.LowerCaseFilter
import org.apache.lucene.analysis.StopFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.standard.StandardFilter
import org.apache.lucene.analysis.standard.StandardTokenizer

@TypeChecked
class SynonymAnalyzer extends Analyzer {
	private SynonymEngine engine

	public SynonymAnalyzer(SynonymEngine engine) {
		this.engine = engine
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		def result = new SynonymFilter(new StopFilter(new LowerCaseFilter(
				new StandardFilter(new StandardTokenizer(reader))),
				StandardAnalyzer.STOP_WORDS), engine)
		return result 
	}
}
