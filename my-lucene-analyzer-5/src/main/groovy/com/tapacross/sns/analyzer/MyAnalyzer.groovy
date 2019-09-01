package com.tapacross.sns.analyzer

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents

import groovy.transform.TypeChecked

@TypeChecked
class MyAnalyzer extends Analyzer {
	private String[] tokens;
	private String[] pos;

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		return new TokenStreamComponents(new MyTokenizer(tokens, pos));
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}
	public void setpos(String[] pos) {
		this.pos = pos;
	}
}
