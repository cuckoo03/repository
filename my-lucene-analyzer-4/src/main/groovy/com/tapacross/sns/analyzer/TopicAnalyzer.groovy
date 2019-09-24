package com.tapacross.sns.analyzer

import java.io.IOException
import java.io.Reader

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents

import groovy.transform.TypeChecked

@TypeChecked
class TopicAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		return new TokenStreamComponents(new TopicTokenizer(reader))
	}
}
