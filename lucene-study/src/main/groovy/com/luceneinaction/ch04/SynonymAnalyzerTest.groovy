package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.PhraseQuery
import org.apache.lucene.search.TermQuery
import org.apache.lucene.store.RAMDirectory
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@TypeChecked
class SynonymAnalyzerTest {
	private RAMDirectory dir
	private IndexSearcher searcher
	private static SynonymAnalyzer synonymAnalyzer = new SynonymAnalyzer(
	new MockSynonymEngine())

	@Before
	public void setUp() {
		dir = new RAMDirectory()

		def writer = new IndexWriter(dir, synonymAnalyzer, true)
		def doc = new Document()
		doc.add(Field.Text("content",
				"The quick borwn fox jumps over the lazy dogs"))
		writer.addDocument(doc)
		writer.close()

		searcher = new IndexSearcher(dir)
	}

	@After
	public void tearDown() {
		searcher.close()
	}

	@Test
	public void testSearchByAPI() {
		def tq = new TermQuery(new Term("content", "speedy"))
		def hits = searcher.search(tq)
		Assert.assertEquals(1, hits.length())

		def pq = new PhraseQuery()
		pq.add(new Term("content", "jumps"))
		pq.add(new Term("content", "above"))
		hits = searcher.search(pq)
		Assert.assertEquals(1, hits.length())
	}

	@Test
	public void testWithQueryParser() {
		def query = QueryParser.parse("\"fox jumps\"", "content",
				synonymAnalyzer)
		def hits = searcher.search(query)
		// fail
//		Assert.assertEquals(0, hits.length())

		query = QueryParser.parse("\"fox jumps\"", "content",
				new StandardAnalyzer())
		hits = searcher.search(query)
		Assert.assertEquals(1, hits.length())
	}
}
