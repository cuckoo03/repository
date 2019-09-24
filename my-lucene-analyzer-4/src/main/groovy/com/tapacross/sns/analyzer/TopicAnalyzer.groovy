package com.tapacross.sns.analyzer

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.util.Version

import groovy.transform.TypeChecked

@TypeChecked
class TopicAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		final def tokenizer = new TopicTokenizer(reader)
		final def filter = new TopicTokenFilter(Version.LUCENE_46, tokenizer)
		return new TokenStreamComponents(tokenizer, filter)
	}
}
