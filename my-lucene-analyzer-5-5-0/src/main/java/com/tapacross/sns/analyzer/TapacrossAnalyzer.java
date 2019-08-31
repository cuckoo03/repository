package com.tapacross.sns.analyzer;

import org.apache.lucene.analysis.Analyzer;

public class TapacrossAnalyzer extends Analyzer {
	private String[] tokens;
	private String[] pos;

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		return new TokenStreamComponents(new TapacrossTokenizer(tokens, pos));
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}
	public void setpos(String[] pos) {
		this.pos = pos;
	}
}
