package com.ml.ciinaction.ch04

import groovy.transform.TypeChecked

import com.ml.ciinaction.ch08.TagMagnitude
import com.ml.ciinaction.ch08.TagMagnitudeImpl

@TypeChecked
class MetaDataVectorImpl implements MetaDataVector {
	private List<TagMagnitude> tagMagnitudeList
	private Map<Long, TagMagnitude> tagMagnitudeMap

	public MetaDataVectorImpl(List<TagMagnitude> tagMagnitudeList) {
		this.tagMagnitudeList = tagMagnitudeList
		this.tagMagnitudeMap = new HashMap<>()
		this.tagMagnitudeList.each { TagMagnitude tm ->
			this.tagMagnitudeMap[tm.getTagId(), tm]
		}
		normalize(this.tagMagnitudeList)
		Collections.sort(this.tagMagnitudeList)
	}

	@Override
	public List<TagMagnitude> getTagMetaDataMagnitude() {
		return this.tagMagnitudeList
	}

	@Override
	public MetaDataVector add(MetaDataVector other) {
		return add(other, 1)
	}

	// must implement
	public MetaDataVector add(MetaDataVector other, double otherScale) {
		return null
	}


	// must implement
	protected List<TagMagnitude> normalize(List<TagMagnitude> tmList) {
		return null
	}
}
