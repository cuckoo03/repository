package com.ml.ciinaction.ch04

import groovy.transform.TypeChecked

@TypeChecked
class SimpleStopWordStemmerMetaDataExtractor
extends SimpleStopWordMetaDataExtractor {
	protected String normalizeToken(String token) {
		// 불용어로 제거된 단어는 정규화 작업을 수행하지 않는다
		if (acceptToken(token)) {
			token = super.normalizeToken(token)

			// 문자열 정규화
			if (token.endsWith("s")) {
				def index = token.lastIndexOf("s")
				if (index > 0)
					token = token.substring(0, index)
			}
		}

		return token
	}
}
