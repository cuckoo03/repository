package com.ml.ciinaction.ch04

import groovy.transform.TypeChecked

import com.ml.ciinaction.ch08.TagMagnitude
import com.ml.ciinaction.ch08.TagMagnitudeImpl

@TypeChecked
class SimpleMetaDataExtractor implements MetaDataExtractor {
	// 발견된 모든 태그의 맵을 유지한다.
	private Map<String, Long> idMap
	// 고유 ID를 생성하기 위해 사용된다.
	private Long currentId

	public SimpleMetaDataExtractor() {
		this.idMap = new HashMap<>()
		this.currentId = new Long(0)
	}

	@Override
	public MetaDataVector extractMetaData(String title, String body) {
		// 제목과 본문에 동일한 가중치를 준다.
		def titleMDV = getMetaDataVector(title)
		def bodyMDV = getMetaDataVector(body)

		return titleMDV.add(bodyMDV)
	}

	protected boolean acceptToken(String token) {
		return true
	}

	// 문장 부호를 제거하고 소문자로 바꾼다
	protected String normalizeToken(String token) {
		def normalizeToken = token.toLowerCase().trim()
		if (normalizeToken.endsWith(".") || normalizeToken.endsWith(",")) {
			def size = normalizeToken.size()
			normalizeToken = normalizeToken.substring(0, size - 1)
		}
		return normalizeToken
	}

	protected MetaDataVector createMetaDataVector(Map<String, Integer> keywordMap) {
		def tmList = new ArrayList<TagMagnitude>()
		for (String tagName : keywordMap.keySet()) {
			def tm = new TagMagnitudeImpl(
					keywordMap.get(tagName), getTokenId(tagName), tagName)
			tmList.add(tm)
		}

		def mdv = new MetaDataVectorImpl(tmList)
		return mdv
	}

	// 발견된 태그에 고유 ID를 부여한다.
	protected Long getTokenId(String token) {
		def id = this.idMap.get(token)
		if (id == null) {
			id = this.currentId++
			this.idMap.put(token, id)
		}
		return id
	}

	private MetaDataVector getMetaDataVector(String text) {
		def keywordMap = new HashMap<String, Integer>()
		// 공백을 기준으로 자른다.
		def st = new StringTokenizer(text)
		while (st.hasMoreTokens()) {
			def token = normalizeToken(st.nextToken())
			// 유효한 토큰인가
			if (acceptToken(token)) {
				def count = keywordMap.get(token)
				if (count == null)
					count = new Integer(0)

				count++
				// 빈도수를 유지한다.
				keywordMap.put(token, count)
			}
		}
		def mdv = createMetaDataVector(keywordMap)
		return mdv
	}
}
