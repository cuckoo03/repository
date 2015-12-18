package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

@TypeChecked
class TagMagnitudeVectorImpl implements TagMagnitudeVector {
	private Map<Tag, TagMagnitude> tagMagnitudesMap

	public TagMagnitudeVectorImpl(List<TagMagnitude> tagMagnitudes) {
		normalize(tagMagnitudes)
	}

	@Override
	public List<TagMagnitude> getTagMagnitudes() {
		def sortedTagMagnitudes = new ArrayList<TagMagnitude>()
		sortedTagMagnitudes.addAll(tagMagnitudeMap.values())
		// 중요도에 따라 정렬
		Collections.sort(sortedTagMagnitudes)
		return sortedTagMagnitudes
	}

	@Override
	public Map<Tag, TagMagnitude> getTagMagnitudeMap() {
		return this.tagMagnitudesMap
	}

	// 태그 벡터와 또 다른 TagMagnitudeVector 사이의 유사도를 구한다.
	@Override
	public double dotProduct(TagMagnitudeVector o) {
		def otherMap = o.getTagMagnitudeMap()
		def dotProduct = 0d
		for (Tag tag : this.tagMagnitudeMap.keySet()) {
			def otherTm = otherMap.get(tag)
			if (otherTm != null) {
				def tm = this.tagMagnitudeMap.get(tag)
				dotProduct += tm.getMagnitude() * otherTm.getMagnitude()
			}
		}
		return dotProduct
	}

	@Override
	public TagMagnitudeVector add(TagMagnitudeVector o) {
		def otherMap = o.getTagMagnitudeMap()
		// 두 벡터 모두에 포함된 태그를 저장할 맵 구조
		def uniqueTags = new HashMap<Tag, Tag>()
		for (Tag tag : this.tagMagnitudeMap.keySet()) {
			uniqueTags.put(tag, tag)
		}
		for (Tag tag : otherMap.keySet()) {
			uniqueTags.put(tag, tag)
		}
		def tagMagnitudesList = new ArrayList<TagMagnitude>(uniqueTags.size())
		uniqueTags.keySet.each  { tag ->
			// 같은 태그에 대해 중요도를 결합한다.
			def tm = mergeTagMagnitudes(this.tagMagnitudeMap.get(tag),
					otherMap.get(tag))
			tagMagnitudesList.add(tm)
		}
		return new TagMagnitudeVectorImpl(tagMagnitudesList)
	}

	@Override
	public TagMagnitudeVector add(Collection<TagMagnitudeVector> tmList) {
		def uniqueTags = new HashMap<Tag, Double>()

		tagMagnitudesMap.values().each { TagMagnitude tagMagnitude ->
			uniqueTags.put(tagMagnitude.getTag(), tagMagnitude.getMagnitudeSqd())
		}

		// 모든 태그에 대해서 반복한다.
		tmList.each { tmv ->
			def tagMap = tmv.getTagMagnitudeMap()
			tagMap.values().each  { tm ->
				def sumSqd = uniqueTags.get(tm.getTag())
				if (sumSqd == null)
					uniqueTags.put(tm.getTag(), tm.getMagnitudeSqd())
				else {
					sumSqd = sumSqd.doubleValue() + tm.getMagnitudeSqd()
					uniqueTags.put(tm.getTag(), sumSqd)
				}
			}
		}

		def newList = new ArrayList<TagMagnitude>()
		uniqueTags.keySet().each { tag ->
			newList.add(new TagMagnitudeImpl(tag, Math.sqrt(uniqueTags.get(tag))))
		}

		return new TagMagnitudeVectorImpl(newList)
	}

	@Override
	public String toString() {
		def sb = new StringBuilder()
		def sortedList = getTagMagnitudes()
		def sumSqd = 0d
		for (TagMagnitude tm : sortedList) {
			sb.append(tm)
			sumSqd = sumSqd.plus(tm.getMagnitude().multiply(tm.getMagnitude()))
		}
		sb.append("\nSumSqd=$sumSqd")
		
		return sb.toString()
	}

	// 입력 리스트를 정규화
	private void normalize(List<TagMagnitude> tagMagnitudes) {
		tagMagnitudesMap = new HashMap<>()
		if ((tagMagnitudes == null) || (tagMagnitudes.size() == 0))
			return
		def sumSqd = 0d
		for (TagMagnitude tm : tagMagnitudes)
			sumSqd = sumSqd.plus(tm.getMagnitudeSqd())

		if (sumSqd == 0d)
			sumSqd = 1d / tagMagnitudes.size()

		// 중요도가 1인 벡터로 만들기 위한 정규화 인자
		def normFactor = Math.sqrt(sumSqd)
		tagMagnitudes.each { tm ->
			def otherTm = this.tagMagnitudeMap.get(tm.getTag())
			def magnitude = tm.getMagnitude()
			if (otherTm != null)
				magnitude = mergeMagnitudes(magnitude,
						otherTm.getMagnitude() * normFactor)

			def normalizedTm = new TagMagnitudeImpl(tm.getTag(),
					(magnitude/ normFactor))
			this.tagMagnitudeMap.put(tm.getTag(), normalizedTm)
		}
	}

	private double mergeMagnitudes(double a, double b) {
		// 두 텀을 결합하는 공식
		return Math.sqrt((a * a) + (b * b))
	}

	private TagMagnitude mergeTagMagnitudes(TagMagnitude a, TagMagnitude b) {
		if (a == null) {
			if (b == null) {
				return null
			}
			return b
		} else if (b == null) {
			return a
		} else {
			def magnitude = mergeMagnitudes(a.getMagnitude(), b.getMagnitude())
			return new TagMagnitudeImpl(a.getTag(), magnitude)
		}
	}
}