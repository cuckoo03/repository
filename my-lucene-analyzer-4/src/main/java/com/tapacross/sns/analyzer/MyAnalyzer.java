package com.tapacross.sns.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;

public class MyAnalyzer extends Analyzer {
	private String[] tokens;
	private String[] pos;

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		return new TokenStreamComponents(new MyTokenizer(reader, tokens, pos));
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}
	public void setpos(String[] pos) {
		this.pos = pos;
	}
}