package com.ml.ciinaction.ch04

import groovy.transform.TypeChecked

@TypeChecked
class SimpleStopWordMetaDataExtractor extends SimpleMetaDataExtractor {
	// 불용어 사전
	private static final String[] stopWords = [
		"and",
		"of",
		"the",
		"to",
		"is",
		"their",
		"can",
		"all",
		""
	]
	
	private Map<String, String> stopWordsMap
	
	public simpleStopWordMetaDataExtractor() {
		this.stopWordsMap = new HashMap<>()
		stopWords.each { String s ->
			this.stopWordsMap.put(s, s)
		}
	}
	
	// 불용어일 경우 토큰으로 받아들이지 않는다
	@Override
	protected boolean acceptToken(String token) {
		return !this.stopWordsMap.containsKey(token)
	}
}
