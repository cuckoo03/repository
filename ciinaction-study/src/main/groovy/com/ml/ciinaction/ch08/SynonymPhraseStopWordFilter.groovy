package com.ml.ciinaction.ch08

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Token
import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream

@TypeChecked
class SynonymPhraseStopWordFilter extends TokenFilter {
	private Stack<Token> injectedTokenStack = null
	private Token previousToken = null
	private SynonymsCache synonymsCache = null
	private PhrasesCache phrasesCache = null

	public SynonymPhraseStopWordFilter(TokenStream input,
	SynonymsCache synonymsCache, PhrasesCache phrasesCache) {
		super(input)
		this.synonymsCache = synonymsCache
		this.phrasesCache = phrasesCache
		this.injectedTokenStack = new Stack<Token>()
	}

	@Override
	public Token next() throws IOException {
		if (this.injectedTokenStack.size() > 0)
			return this.injectedTokenStack.pop()
		def token = input.next()
		if (token != null) {
			def phrase = injectPhrase(token)
			injectSynonyms(token.termText(), token)
			injectSynonyms(phrase, token)
			this.previousToken = token
		}

		return token
	}

	// 구와 동의어를 추가
	private String injectPhrase(Token currentToken) throws IOException {
		if (this.previousToken != null) {
			def phrase = this.previousToken.termText() + " " +
					currentToken.termText()
			if (this.phrasesCache.isValidPhrase(phrase)) {
				def phraseToken = new Token(phrase, currentToken.startOffset(),
						currentToken.endOffset(), "phrase")
				phraseToken.setPositionIncrement(0)
				this.injectedTokenStack.push(phraseToken)
				return phrase
			}
		}
		return null
	}
	private void injectSynonyms(String text, Token currentToken)
	throws IOException {
		if (text != null) {
			def synonyms = this.synonymsCache.getSynonym(text)
			if (synonyms != null) {
				synonyms.each {synonym ->
					def synonymToken = new Token(synonym,
							currentToken.startOffset(),
							currentToken.endOffset(), "synonym")
					synonymToken.setPositionIncrement(0)
					this.injectedTokenStack.push(synonymToken)
				}
			}
		}
	}
}
