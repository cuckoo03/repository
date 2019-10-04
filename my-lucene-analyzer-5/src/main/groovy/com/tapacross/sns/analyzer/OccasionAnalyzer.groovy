package com.tapacross.sns.analyzer

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.util.Version

import groovy.transform.TypeChecked

/**
 * TPO 분석기 
 */
@TypeChecked
class OccasionAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final def tokenizer = new OccasionTokenizer()
		final def filter = new OccasionTokenFilter(tokenizer)
		return new TokenStreamComponents(tokenizer, filter)
	}
}
