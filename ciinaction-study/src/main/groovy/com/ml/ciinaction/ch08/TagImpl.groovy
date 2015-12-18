package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked;

@TypeChecked
class TagImpl implements Tag {
	private String displayText
	private String stemmedText
	private int hashCode
	
	// 불변 객체
	public TagImpl(String displayText, String stemmedText) {
		this.displayText = displayText
		this.stemmedText = stemmedText
		// 빠른 검색을 위핸 해스 코드를 미리 계산
		this.hashCode = stemmedText.hashCode() 
	}
	
	@Override
	public String getDisplayText() {
		return this.displayText
	}

	@Override
	public String getStemmedText() {
		return this.stemmedText
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.hashCode == obj.hashCode())
	}
	
	@Override
	public int hashCode() {
		return this.hashCode
	}
	
	@Override
	public String toString() {
		return "[ $displayText, $stemmedText]"
	}
}
