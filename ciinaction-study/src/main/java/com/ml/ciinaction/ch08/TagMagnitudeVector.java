package com.ml.ciinaction.ch08;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TagMagnitudeVector {
	public List<TagMagnitude> getTagMagnitudes();

	public Map<Tag, TagMagnitude> getTagMagnitudeMap();

	// 두 텀벡터의 내적을 구한다
	public double dotProduct(TagMagnitudeVector o);

	// 두 텀벡터를 결합한다
	public TagMagnitudeVector add(TagMagnitudeVector o);

	// 텀벡터 컬렉션을 결합한다
	public TagMagnitudeVector add(Collection<TagMagnitudeVector> tmList);
}
