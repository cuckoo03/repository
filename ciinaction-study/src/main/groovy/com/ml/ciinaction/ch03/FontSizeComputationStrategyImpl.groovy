package com.ml.ciinaction.ch03

import java.util.List

import groovy.transform.TypeChecked

@TypeChecked
abstract class FontSizeComputationStrategyImpl implements FontSizeComputationStrategy {
	// double 형의 동일함을 체크하기 위핸  사용
	private static final double PRECISION = 0.00001
	private Integer numSizes
	private String prefix

	public FontSizeComputationStrategyImpl(int numSizes, String prefix) {
		this.numSizes = numSizes
		this.prefix = prefix
	}

	@Override
	public void computeFontSize(List<TagCloudElement> elements) {
		if (elements.size() > 0) {
			def Double minCount = null
			def Double maxCount = null
			for (TagCloudElement tce : elements) {
				def n = tce.getWeight()
				// 최대, 최소 빈도수를 계산한다.
				if ((minCount == null) || (minCount > n))
					minCount = n
				if ((maxCount == null) || (maxCount < n))
					maxCount = n
			}
			// 빈도수를 스케일링한다
			def maxScaled = scaleCount(maxCount)
			def minScaled = scaleCount(minCount)
			def diff = (maxScaled - minScaled) / this.numSizes

			// 폰트 크기 지정
			elements.each { tce ->
				// 적당한 폰트 버킷을 할당한다
				def index = (int) Math.floor((scaleCount(tce.getWeight()) - minScaled) / diff)
				if (Math.abs(tce.getWeight() - maxCount) < PRECISION)
					index = this.numSizes - 1

				tce.setFontSize(this.prefix + index)
			}
		}
	}

	public int getNumSizes() {
		return this.numSizes
	}

	public String getPrefix() {
		return this.prefix
	}

	@Override
	public String toString() {
		return "numSize:$numSizes, prefix:$prefix"
	}
	
	// 상속받는 클래스에서 구현한게끔 추상클래스의 멤버 함수로 설계한다.
	protected abstract double scaleCount(double count)
}
