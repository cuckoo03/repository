package com.luceneinaction.ch03

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TermQuery
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore;
import org.junit.Test

@TypeChecked
class SearchingTest {
	private Directory dir

	@Before
	public void setUp() {
		def indexDir = System.getProperty("java.io.tmpdir", "tmp") +
				System.getProperty("file.separator") + "index-dir"
		dir = FSDirectory.getDirectory(indexDir, true)

		def writer = new IndexWriter(dir, new SimpleAnalyzer(), true)

		def doc = new Document()
		doc.add(Field.Text("subject", "ant"))
		doc.add(Field.Text("subject2", "junit junit"))
		writer.addDocument(doc)

		writer.optimize()
		writer.close()
	}

	@Test
	public void testTerm() {
		def searcher = new IndexSearcher(dir)
		def t = new Term("subject", "ant")
		def query = new TermQuery(t)
		def hits = searcher.search(query)
		Assert.assertEquals(1, hits.length())

		t = new Term("subject2", "junit")
		hits = searcher.search(new TermQuery(t))
		Assert.assertEquals(1, hits.length())

		searcher.close()
	}

	@Test
	public void testQueryParser() {
		def searcher = new IndexSearcher(dir)
		def query = QueryParser.parse("junit or ant", "subject",
				new SimpleAnalyzer())
		def hits = searcher.search(query)
		Assert.assertEquals(1, hits.length())
		def doc = hits.doc(0)
		Assert.assertEquals("ant", doc.get("subject"))
	}
}
