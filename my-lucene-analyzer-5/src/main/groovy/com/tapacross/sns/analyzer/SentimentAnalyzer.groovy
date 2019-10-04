package com.tapacross.sns.analyzer

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.util.Version

import groovy.transform.TypeChecked

/**
 * 감성어 분석기 
 */
@TypeChecked
class SentimentAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final def tokenizer = new SentimentTokenizer()
		final def filter = new SentimentTokenFilter(tokenizer)
		return new TokenStreamComponents(tokenizer, filter)
	}
}
