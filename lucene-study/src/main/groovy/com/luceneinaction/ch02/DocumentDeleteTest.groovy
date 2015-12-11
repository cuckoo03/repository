package com.luceneinaction.ch02

import groovy.transform.TypeChecked

import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.junit.Assert
import org.junit.Test

@TypeChecked
class DocumentDeleteTest extends BaseIndexingTest {
	@Test
	public void test() {
//		testDeleteBeforeIndexMerge()
		testDeleteAfterIndexMerge()
	}

	public void testDeleteBeforeIndexMerge() {
		def reader = IndexReader.open(dir)
		Assert.assertEquals(2, reader.maxDoc())
		Assert.assertEquals(2, reader.numDocs())
		reader.delete(1)

		Assert.assertTrue(reader.isDeleted(1))
		Assert.assertTrue(reader.hasDeletions())
		Assert.assertEquals(2, reader.maxDoc())
		Assert.assertEquals(1, reader.numDocs())
		
		reader.close()
		
		reader = IndexReader.open(dir)
		
		Assert.assertEquals(2, reader.maxDoc())
		Assert.assertEquals(1, reader.numDocs())
		
		reader.close()
	}
	
	public void testDeleteAfterIndexMerge() {
		def reader = IndexReader.open(dir)
		Assert.assertEquals(2, reader.maxDoc())
		Assert.assertEquals(2, reader.numDocs())
//		reader.delete(1) id로 삭제
		reader.delete(new Term("id", "1")) //필드와 단어로 삭제
		reader.close()
		
		def writer = new IndexWriter(dir, getAnalyzer(), false)
		writer.optimize()
		writer.close()

		reader = IndexReader.open(dir)
		
		Assert.assertFalse(reader.isDeleted(1))
		Assert.assertFalse(reader.hasDeletions())
		Assert.assertEquals(1, reader.maxDoc())
		Assert.assertEquals(1, reader.numDocs())
		
		reader.close()
	}
}