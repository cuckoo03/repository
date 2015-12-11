package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Token
import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream

@TypeChecked
class PositionalStopFilter extends TokenFilter {
	private Set<String> stopWords

	public PositionalStopFilter(TokenStream tokenStream, Set stopWords) {
		super(tokenStream)
		this.stopWords = stopWords
	}

	@Override
	public Token next() {
		def increment = 0;
		for (Token token = input.next(); token != null; token = input.next()) {
			if (!stopWords.contains(token.termText())) {
				token.setPositionIncrement(token.getPositionIncrement()
						+ increment)
				return token
			}
			increment++
		}
		return null;
	}
}
