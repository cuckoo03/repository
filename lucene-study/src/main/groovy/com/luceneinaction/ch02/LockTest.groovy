package com.luceneinaction.ch02

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore;
import org.junit.Test

@TypeChecked
class LockTest {
	private Directory dir
	@Before
	public void setUp() {
		def indexDir = System.getProperty("java.io.tmp", "tmp") +
				System.getProperty("file.separator") + "index-dir"
		dir = FSDirectory.getDirectory(indexDir, true)
	}

	@Test
	public void testWriteLock() {
		def writer1 = null
		def writer2 = null

		try {
			writer1 = new IndexWriter(dir, new SimpleAnalyzer(), true)
			writer2 = new IndexWriter(dir, new SimpleAnalyzer(), true)
			Assert.fail("reach this point")
		} catch (IOException e) {
			e.printStackTrace()
		}finally {
			writer1.close()
			Assert.assertNull(writer2)
		}
	}

	@Test
	public void testCommitLock() {
		def reader1 = null
		def reader2 = null

		try {
			def writer = new IndexWriter(dir, new SimpleAnalyzer(), true)
			writer.close()
			reader1 = IndexReader.open(dir)
			reader2 = IndexReader.open(dir)
		} finally {
			reader1.close()
			reader2.close()
		}
	}
}