package com.luceneinaction.ch02

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.junit.Before
import org.junit.Test

@TypeChecked
class BaseIndexingTest {
	protected String[] keywords = ["1", "2"]
	protected String[] unindexed = ["Netherlands", "italy"]
	protected String[] unstored = ["Amsterdam has", "Venice has"]

	protected String[] text = ["Amsterdam", "Venice"]
	protected Directory dir

	@Before
	public void setUp() {
		def indexDir = System.getProperty("java.io.tmpdir") +
				System.getProperty("file.separator") + "index-dir"

		dir = FSDirectory.getDirectory(indexDir, true)
		addDocuments(dir)
		println "setup"
	}

	@Test
	public void test() {
		println "test"
	}

	private void addDocuments(Directory dir) {
		def writer = new IndexWriter(dir, getAnalyzer(), true)
		writer.setUseCompoundFile(isCompound())
		for (int i = 0; i < keywords.size(); i++) {
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

	protected org.apache.lucene.analysis.Analyzer getAnalyzer() {
		return new SimpleAnalyzer()
	}

	protected boolean isCompound() {
		return true
	}
}
