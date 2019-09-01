package com.tapacross.sns.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public class MyTokenFilter extends TokenFilter {
	public MyTokenFilter(TokenStream input) {
		super(input);
	  }

	@Override
	public boolean incrementToken() throws IOException {
		return false;
	}
}

