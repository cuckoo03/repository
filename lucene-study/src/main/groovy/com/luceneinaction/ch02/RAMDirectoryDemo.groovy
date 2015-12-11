package com.luceneinaction.ch02

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.RAMDirectory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@TypeChecked
class RAMDirectoryDemo {
	private Directory fsDir
	private Directory ramDir
	private Collection docs = loadDocuments(3000, 5)
	private int mergeFactor = 2147483647
	private int maxMergeDocs = 10//10
	private int minMergeDocs = 10//10

	@Before
	public void setUp() {
		def fsIndexDir = System.getProperty("java.io.tmpdir", "tmp") +
				System.getProperty("file.separator") + "fs-index"

		ramDir = new RAMDirectory()
		fsDir = FSDirectory.getDirectory(fsIndexDir, true)
	}

	@Test
	public void testTiming() {
		def ramTiming = timeIndexWriter(ramDir)
		def fsTiming = timeIndexWriter(fsDir)

		Assert.assertTrue(fsTiming > ramTiming)

		println "RAMDirectory Time: ($ramTiming) ms"
		println "FSDirectory Time: ($fsTiming) ms"
	}

	private long timeIndexWriter(Directory dir) {
		def start = System.currentTimeMillis()
		addDocuments(dir)
		def stop = System.currentTimeMillis()
		return stop - start
	}

	private void addDocuments(Directory dir) {
		def writer = new IndexWriter(dir, new SimpleAnalyzer(), true)
		// 색인 성능을 조절하는 환경변수 설정
		writer.mergeFactor = this.mergeFactor
		writer.maxMergeDocs = this.maxMergeDocs
		writer.minMergeDocs = this.minMergeDocs

		Iterator<String> iter = docs.iterator()

		while (iter.hasNext()) {
			def doc = new Document()
			def word = iter.next()
			doc.add(Field.Keyword("keyword", word))
			doc.add(Field.UnIndexed("unindexed", word))
			doc.add(Field.UnStored("unstored", word))
			doc.add(Field.Text("text", word))
			writer.addDocument(doc)
		}
		writer.optimize()
		writer.close()
	}

	private Collection loadDocuments(int numDocs, int wordsPerDoc) {
		def docs = new ArrayList(numDocs)
		for (int i = 0; i < numDocs; i++) {
			def doc = new StringBuffer(wordsPerDoc)
			for (int j = 0; j < wordsPerDoc; j++) {
				doc.append("Bibamus ")
			}
			docs.add(doc.toString())
		}
		return docs
	}
}
