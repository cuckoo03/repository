package com.ml.ciinaction.ch04

import groovy.transform.TypeChecked

@TypeChecked
class SimpleBiTermStopWordStemmerMetaDataExtractor
extends SimpleStopWordStemmerMetaDataExtractor{
	protected MetaDataVector getMetaDataVector(String text) {
		def keywordMap = new HashMap<String, Integer>()
		def allTokens = new ArrayList<String>()
		def st = new StringTokenizer(text)
		while (st.hasMoreTokens()) {
			def token = normalizeToken(st.nextToken())
			if (acceptToken(token)) {
				def count = keywordMap.get(token)
				if (count == null) {
					count = 0
				}
				count++
				keywordMap.put(token, count)
				// 순서대로 정규화된 키워드를 저장한다
				allTokens.add(token)
			}
		}
		def firstToken = allTokens.get(0)
		allTokens.subList(1, allTokens.size()).each { token ->
			def biTerm = firstToken + " " + token
			// 두 토큰을 입력받아 유효성을 검사한다
			if (isValidBiTermToken(biTerm)) {
				def count = keywordMap.get(biTerm)
				if (count == null)
					count = 0
				count++
				keywordMap.put(biTerm, count)
			}
			firstToken = token
		}
		def mdv = createMetaDataVector(keywordMap)
		return mdv
	}
	
	// 구 사전을 이용해 입력된 구 후보를 테스트한다
	private boolean isValidBiTermToken(String biTerm) {
		if ("collective intelligence".compareTo(biTerm) == 0)
			return true 
		return false
	}
}
