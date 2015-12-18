package com.ml.ciinaction.ch03

import groovy.transform.TypeChecked

@TypeChecked
class TagCloudImpl implements TagCloud {
	private List<TagCloudElement> elements

	public TagCloudImpl(List<TagCloudElement> elements,
	FontSizeComputationStrategy strategy) {
		this.elements = elements
		// 폰트 크기를 계산
		strategy.computeFontSize(this.elements)
		// 태그를 알파벳 순서로 정렬
		Collections.sort(this.elements)
	}

	@Override
	public List<TagCloudElement> getTagCloudElements() {
		return this.elements
	}
}
