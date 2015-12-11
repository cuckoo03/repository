package com.luceneinaction.ch02

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TermQuery
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@TypeChecked
class FieldLengthTest {
	private Directory dir
	private String[] keywords = ["1", "2"]
	private String[] unindexed = ["Netherlands", "Italy"]
	private String[] unstored = ["Amsterdam has bridges", "Venice has canals"]
	private String[] text = ["Amsterdam", "Venice"]

	@Before
	public void setUp() {
		def indexDir = System.getProperty("java.io.tmpdir", "tmp") +
				System.getProperty("file.separator") + "index-dir"
		dir = FSDirectory.getDirectory(indexDir, true)
	}

	@Test
	public void testFieldSize() {
		addDocuments(dir, 2)
		Assert.assertEquals(1, getHitCount("contents", "bridges"))

		addDocuments(dir, 1)
		Assert.assertEquals(0, getHitCount("contents", "bridges"))
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

	private void addDocuments(Directory dir, int maxFieldLength) {
		def writer = new IndexWriter(dir, new SimpleAnalyzer(), true)
		writer.infoStream = System.out
		writer.maxFieldLength = maxFieldLength;
		for (int i = 0; i < keywords.length; i++) {
			def doc = new Document()
			doc.add(Field.Keyword("id", keywords[i]))
			doc.add(Field.UnIndexed("country", unindexed[i]))
			doc.add(Field.UnStored("contents", unstored[i]))
			doc.add(Field.Text("city", text[i]))
			writer.addDocument(doc)
		}
		writer.optimize()
		writer.close()
	}
}
