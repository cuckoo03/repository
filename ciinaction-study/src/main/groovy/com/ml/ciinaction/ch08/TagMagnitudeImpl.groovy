package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

@TypeChecked
class TagMagnitudeImpl implements TagMagnitude {
	private Tag tag
	private double magnitude
	private Long tagId
	private String tagText

	public TagMagnitudeImpl(Tag tag, double magnitude) {
		this.tag = tag
		this.magnitude = magnitude
	}
	
	public TagMagnitudeImpl(double magnitude, long tagId, String tagText) {
		this.magnitude = magnitude
		this.tagId = tagId
		this.tagText = tagText
	}

	@Override
	public String getDisplayText() {
		return this.tag.getDisplayText()
	}

	@Override
	public String getStemmedText() {
		return this.tag.getStemmedText()
	}

	// 주요도에 따라 정렬할 때 유용
	@Override
	public int compareTo(TagMagnitude o) {
		def diff = this.magnitude - o.getMagnitude()
		if (diff > 0)
			return -1
		else if (diff < 0)
			return 1
		return 0
	}

	@Override
	public double getMagnitude() {
		return this.magnitude
	}

	@Override
	public double getMagnitudeSqd() {
		return this.magnitude * this.magnitude
	}

	@Override
	public Tag getTag() {
		return this.tag
	}

	@Override
	public String toString() {
		return "[" +tag.getDisplayText() + ", " + tag.getStemmedText() + ", "+
				getMagnitude() + "]"
	}

	@Override
	public String getTagText() {
		return this.tag.getDisplayText()
	}

	@Override
	public Long getTagId() {
		return tagId
	}
}
