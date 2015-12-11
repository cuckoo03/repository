package com.luceneinaction.ch06

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.WhitespaceAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.Sort
import org.apache.lucene.search.SortField
import org.apache.lucene.search.TermQuery
import org.apache.lucene.store.RAMDirectory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@TypeChecked
class DistanceSortingTest {
	private RAMDirectory directory
	private IndexSearcher searcher
	private Query query

	@Before
	public void setUp() {
		directory = new RAMDirectory()
		def writer = new IndexWriter(
				directory, new WhitespaceAnalyzer(), true)
		addPoint(writer, "El Charro", "restaurant", 1, 2)
		addPoint(writer, "Cafe Poca Cosa", "restaurant", 5, 9)
		addPoint(writer, "Los Betos", "restaurant", 9, 6)
		addPoint(writer, "Nico Taco Shop", "restaurant", 3, 8)

		writer.close()

		searcher = new IndexSearcher(directory)
		query = new TermQuery(new Term("type", "restaurant"))
	}

	private void addPoint(IndexWriter writer, String name, String type,
			int x, int y) {
		def doc = new Document()
		doc.add(Field.Keyword("name", name))
		doc.add(Field.Keyword("type", type))
		doc.add(Field.Keyword("location", x + "," + y))
		writer.addDocument(doc)
	}

	@Test
	public void testNearestRestaurantToHome() {
		def sort = new Sort(new SortField("location",
				new DistanceComparatorSource(0, 0)))

		def hits = searcher.search(query, sort)

		Assert.assertEquals("closest", "El Charro", hits.doc(0).get("name"))
		Assert.assertEquals("furthest", "Nico Taco Shop", hits.doc(1).get("name"))
		Assert.assertEquals("furthest", "Los Betos", hits.doc(3).get("name"))
	}
}