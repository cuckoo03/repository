package com.ml.ciinaction.ch03

import groovy.transform.TypeChecked;

@TypeChecked
class TagCloudElementImpl implements TagCloudElement {
	private String fontSize
	private Double weight
	private String tagText
	
	public TagCloudElementImpl(String tagText, double tagCount) {
		this.tagText = tagText
		this.weight = tagCount
	}

	@Override
	public int compareTo(TagCloudElement o) {
		this.tagText.compareTo(o.getTagText())
	}

	@Override
	public String getTagText() {
		return this.tagText
	}

	@Override
	public double getWeight() {
		return this.weight
	}

	@Override
	public String getFontSize() {
		return this.fontSize
	}

	@Override
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize
	}
}
