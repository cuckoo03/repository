package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

import org.junit.Test

@TypeChecked
class BasicOperationTest {
	@Test
	public void basicOperationsTest() throws Exception {
		def tagCache = new TagCacheImpl()
		def tmList = new ArrayList<TagMagnitude>()
		tmList.add(new TagMagnitudeImpl(tagCache.getTag("a"), 1d))
		tmList.add(new TagMagnitudeImpl(tagCache.getTag("b"), 2d))
		tmList.add(new TagMagnitudeImpl(tagCache.getTag("c"), 1.5))
		tmList.add(new TagMagnitudeImpl(tagCache.getTag("a"), 1d))
		def tmVector1 = new TagMagnitudeVectorImpl(tmList)
		println tmVector1
	}
}
