package com.ml.ciinaction.ch03;

public interface TagCloudElement extends Comparable<TagCloudElement> {
	public String getTagText();
	// 가중치를 표현하기 위해 double형 리턴
	public double getWeight();
	public String getFontSize();
	public void setFontSize(String fontSize);
	
}
