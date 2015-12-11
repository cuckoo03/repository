package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.commons.codec.EncoderException
import org.apache.commons.codec.language.Metaphone
import org.apache.lucene.analysis.Token
import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream

@TypeChecked
class MetaphoneReplacementFilter extends TokenFilter {
	public static final String METAPHONE = "METAPHONE"

	private Metaphone metaphone = new Metaphone()

	public MetaphoneReplacementFilter(TokenStream input) {
		super(input)
	}

	@Override
	public Token next() {
		def t = input.next()
		if (t == null)
			return null

		try {
			return new Token(metaphone.encode(t.termText()),
					t.startOffset(), t.endOffset(), METAPHONE)
		} catch (EncoderException e) {
			return t
		}
	}
}