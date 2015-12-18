package com.ml.ciinaction.ch08

import java.io.IOException

import groovy.transform.TypeChecked

@TypeChecked
class PhrasesCacheImpl extends CacheImpl implements PhrasesCache {
	private Map<String, String> validPhrases

	public PhrasesCacheImpl() throws IOException {
		validPhrases = new HashMap<>()
		validPhrases.put(getStemmedText("collective intelligence"), null)
	}
	
	@Override
	public boolean isValidPhrase(String text) throws IOException {
		return this.validPhrases.containsKey(getStemmedText(text))
	}
}
