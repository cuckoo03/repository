package com.ml.ciinaction.ch08;

public interface TagMagnitude extends Tag, Comparable<TagMagnitude>{
	// 중요도 값 얻기
	public double getMagnitude();
	// 중요도의 제곱을 얻기
	public double getMagnitudeSqd();
	// 관련되 Tag객체를 얻기
	public Tag getTag();
}
