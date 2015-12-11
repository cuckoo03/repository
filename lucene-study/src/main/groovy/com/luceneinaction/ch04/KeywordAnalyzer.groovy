package com.luceneinaction.ch04

import groovy.transform.TypeChecked;

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Token
import org.apache.lucene.analysis.TokenStream

/**
 * 전체 스트림을 하나의 토큰으로 토큰화한다.
 * @author cuckoo03
 *
 */
@TypeChecked
class KeywordAnalyzer extends Analyzer {
	@Override
	public TokenStream tokenStream(String fieldName, final Reader reader) {
		return new TokenStream() {
					private boolean done
					private final char[] buffer  = new char[1024]

					@Override
					public Token next() {
						if (!done) {
							done = true
							def buffer = new StringBuffer()
							def length = 0
							while (true) {
								length = reader.read(this.buffer)
								if (length == -1)
									break

								buffer.append(this.buffer, 0, length)
							}
							def text = buffer.toString()
							return new Token(text, 0, text.length())
						}
						return null
					}
				}
	}
}
