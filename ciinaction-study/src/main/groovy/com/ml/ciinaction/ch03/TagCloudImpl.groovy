package com.ml.ciinaction.ch03

import com.ml.ciinaction.ch04.MetaDataVector

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

	public TagCloudImpl(MetaDataVector metaDataVector,
	FontSizeComputationStrategy strategy) {
		this(getTagCloudElements(metaDataVector), strategy)
	}

	@Override
	public List<TagCloudElement> getTagCloudElements() {
		return this.elements
	}

	public static List<TagCloudElement> getTagCloudElements(MetaDataVector
			metaDataVector) {
		def list = new ArrayList<TagCloudElement>()
		metaDataVector.getTagMetaDataMagnitude().each { tm ->
			list.add(new TagCloudElementImpl(tm.getTagText(), tm.getMagnitude()))
		}

		return null
	}
}
