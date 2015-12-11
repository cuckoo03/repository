package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.RAMDirectory
import org.junit.Assert
import org.junit.Test

@TypeChecked
class MetaphoneTest {
	@Test
	public void testKooKat() {
		def dir = new RAMDirectory()
		def analyzer = new MetaphoneReplacementAnalyzer()

		def writer = new IndexWriter(dir, analyzer, true)
		def doc = new Document()
		doc.add(Field.Text("contents", "cool cat"))
		writer.addDocument(doc)
		writer.close()

		def searcher = new IndexSearcher(dir)
//		def query = QueryParser.parse("kool kat", "contents", analyzer)
//		def query = QueryParser.parse("kul ket", "contents", analyzer)
		def query = QueryParser.parse("cul kat", "contents", analyzer)

		def hits = searcher.search(query)

		Assert.assertEquals(1, hits.length())
		Assert.assertEquals("cool cat", hits.doc(0).get("contents"))
	}

	@Test
	public void test() {
		def analyzer = new MetaphoneReplacementAnalyzer()

		AnalyzerUtils.displayTokens(analyzer,
				"the quick brown fox jumped over the lazy dogs")
		println ""
		AnalyzerUtils.displayTokens(analyzer,
				"tha quik brown phox jumpd ovvar tha lazi dogz")
	}
}
