package com.elasticsearch.client

import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class ESManagerTest {
	@Test
	def void testSearchDocument() {
		def manager = new ESManager()
		manager.init()
		def collection = "twitter"
		def searchQuery = "rt"
		def from = "20190101"
		def to = "20190102"
		def offset = 0
		def size = 16
		def result = manager.searchDocument(collection, searchQuery, from, to, 
			offset, size)
		assert result.totalHits > 0
		assert result.hits.size() == 16
	}
	
	@Test
	def void testSearchTrend() {
		def manager = new ESManager()
		manager.init()
		def collection = "twitter"
		def searchQuery = "rt"
		def from = "20190101"
		def to = "20190102"
		def result = manager.searchTrend(collection, searchQuery, from, to)	
		assert result.buckets.size() > 0
		assert result.buckets.size() == 16
	}
}
