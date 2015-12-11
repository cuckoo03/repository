package com.luceneinaction.ch04

import groovy.transform.TypeChecked;

import java.io.IOException

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;

@TypeChecked
class KoreanStemFilter extends TokenFilter {
	private static final String[] WORD_ENDING = ["있네요", "있습니다", "있을까요", "나네", "없다"]

	@Override
	public Token next() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
