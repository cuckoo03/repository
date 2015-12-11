package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Token
import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream

@TypeChecked
class SynonymFilter extends TokenFilter {
	public static final String TOKEN_TYPE_SYNONYM = "SYNONYM"

	private Stack synonymStack
	private SynonymEngine engine

	public SynonymFilter(TokenStream stream, SynonymEngine engine) {
		super(stream)
		this.synonymStack = new Stack() //  유의어 보관 장소
		this.engine = engine
	}

	@Override
	def Token next() {
		// 보관해둔 유의어 중 하나를 뽑아냄
		if (synonymStack.size() > 0)
			return (Token) synonymStack.pop()

		def token = input.next()
		if (token == null)
			return null

		// 읽어들인 토큰의 유의어들을 보관
		addAliasesToStack(token)

		return token
	}


	private void addAliasesToStack(Token token) {
		// 유의어 목록 초기화
		def synonyms = engine.getSynonyms(token.termText())

		if (synonyms == null)
			return

		synonyms.each {it ->
			def synToken = new Token((String)it, token.startOffset(),  0)
			// 위치증가값을 0으로 고정
			synToken.setPositionIncrement(0)

			synonymStack.push(synToken)
		}
	}
}