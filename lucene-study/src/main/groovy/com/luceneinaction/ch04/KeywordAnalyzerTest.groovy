package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.PerFieldAnalyzerWrapper
import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TermQuery
import org.apache.lucene.store.RAMDirectory
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Field.Keyword로 색인한 내용을 검색
 * @author cuckoo03
 *
 */
@TypeChecked
class KeywordAnalyzerTest {
	private RAMDirectory dir
	private IndexSearcher searcher

	@Before
	public void setUp() {
		dir = new RAMDirectory()
		def writer = new IndexWriter(dir, new SimpleAnalyzer(), true)

		def doc = new Document()
		doc.add(Field.Keyword("partnum", "Q36"))
		doc.add(Field.Text("description", "Illidium Space Mudulator"))
		writer.addDocument(doc)

		writer.close()

		searcher = new IndexSearcher(dir)
	}

	@Test
	public void testTermQuery() {
		def query = new TermQuery(new Term("partnum", "Q36"))
		def hits = searcher.search(query)
		Assert.assertEquals(1, hits.length())
	}

	@Test
	public void testBasicQueryParser() {
		def query = QueryParser.parse("partnum:Q36 AND SPACE",
				"description", new SimpleAnalyzer())

		def hits = searcher.search(query)
		Assert.assertEquals("note Q36->q", "+partnum:q +space",
				query.toString("description"))
		Assert.assertEquals("doc not found:(", 0, hits.length())
	}

	@Test
	public void testPerFieldAnalyzer() {
		def analyzer = new PerFieldAnalyzerWrapper(new SimpleAnalyzer())
		analyzer.addAnalyzer("partnum", new KeywordAnalyzer())

		def query = QueryParser.parse("partnum:Q36 AND SPACE", "description",
				, analyzer)

		def hits = searcher.search(query)
		Assert.assertEquals("Q36 kept as-is", "+partnum:Q36 +space",
				query.toString("description"))
		Assert.assertEquals("doc found", 1, hits.length())
	}
}