package com.tapacross.sns.analyzer

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.util.Version

import groovy.transform.TypeChecked

/**
 * 주제어 분석기 
 */
@TypeChecked
class TopicAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final def tokenizer = new TopicTokenizer()
		final def filter = new TopicTokenFilter(tokenizer)
		return new TokenStreamComponents(tokenizer, filter)
	}
}
