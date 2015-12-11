package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.LetterTokenizer
import org.apache.lucene.analysis.TokenStream

@TypeChecked
class MetaphoneReplacementAnalyzer extends Analyzer {
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new MetaphoneReplacementFilter(new LetterTokenizer(reader))
	}
}
