package com.ml.ciinaction.ch08

import java.io.IOException;

import groovy.transform.TypeChecked;

@TypeChecked
class TagCacheImpl extends CacheImpl implements TagCache {
	private Map<String, Tag> tagMap
	
	public TagCacheImpl() {
		this.tagMap = new HashMap<>()
	}
	
	@Override
	public Tag getTag(String text) throws IOException {
		def tag = this.tagMap.get(text)
		if (tag == null) {
			// 어근을 사용해 인스턴스를 찾는다
			def stemmedText = getStemmedText(text)
			tag = new TagImpl(text, stemmedText)
			this.tagMap.put(stemmedText, tag)
		}
		return tag
	}

}
