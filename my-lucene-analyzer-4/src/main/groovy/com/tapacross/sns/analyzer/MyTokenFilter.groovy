package com.tapacross.sns.analyzer
import org.apache.lucene.analysis.TokenStream


import java.io.IOException

import org.apache.lucene.analysis.TokenFilter

import groovy.transform.TypeChecked

@TypeChecked
class MyTokenFilter extends TokenFilter {
	protected MyTokenFilter(TokenStream input) {
		super(input);
	  }
	  
	@Override
	public final boolean incrementToken() throws IOException {
		return super.incrementToken()
	}
}
