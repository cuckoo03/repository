package com.luceneinaction.ch02

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.WhitespaceAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TermQuery
import org.junit.Assert
import org.junit.Test

@TypeChecked
class DocumentUpdateTest extends BaseIndexingTest {
	@Test
	public void test() {
		testUpdate()
	}

	private void testUpdate() {
		Assert.assertEquals(1, getHitCount("city", "Amsterdam"))

		def reader = IndexReader.open(dir)
		reader.delete(new Term("city", "Amsterdam"))
		reader.close()

		Assert.assertEquals(0, getHitCount("city", "Amsterdam"))

		def writer = new IndexWriter(dir, getAnalyzer(), false)
		def doc = new Document()
		doc.add(Field.Keyword("id", "1"))
		doc.add(Field.UnIndexed("country", "Netherlands"))
		doc.add(Field.UnStored("contents", "Amsterdam has"))
		doc.add(Field.Text("city", "Haag"))
		writer.addDocument(doc)
		writer.optimize()
		writer.close()
		Assert.assertEquals(1, getHitCount("city", "Haag"))
	}

	@Override
	protected Analyzer getAnalyzer() {
		return new WhitespaceAnalyzer()
	}

	private int getHitCount(String fieldName, String searchString) {
		def searcher = new IndexSearcher(dir)
		def t = new Term(fieldName, searchString)
		def query = new TermQuery(t)
		def hits = searcher.search(query)
		def hitCount = hits.length()
		searcher.close()
		return hitCount
	}
}
