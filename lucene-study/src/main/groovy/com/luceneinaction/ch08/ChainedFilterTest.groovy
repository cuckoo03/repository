package com.luceneinaction.ch08

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.WhitespaceAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.search.BooleanQuery
import org.apache.lucene.search.DateFilter
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.QueryFilter
import org.apache.lucene.search.TermQuery
import org.apache.lucene.store.RAMDirectory
import org.junit.Before
import org.junit.Test;

import com.lucene.LiaTestCase

@TypeChecked
class ChainedFilterTest {
	public static final int MAX = 500
	private RAMDirectory directory
	private IndexSearcher searcher
	private Query query
	private DateFilter dateFilter
	private QueryFilter bobFilter
	private QueryFilter sueFilter

	@Before
	public void setUp() {
		directory = new RAMDirectory()
		def writer = new IndexWriter(directory, new WhitespaceAnalyzer(), true)

		def cal = Calendar.getInstance()
		cal.setTimeInMillis(1041397200000L)

		0..MAX.each { i->
			def doc = new Document()
			doc.add(Field.Keyword("key", (i as int).plus(1).toString()))
			doc.add(Field.Keyword("owner", ((i as int) < MAX / 2) ? "bob" : "sue"))
			doc.add(Field.Keyword("date", cal.getTime()))
			writer.addDocument(doc)

			cal.add(Calendar.DATE, 1)
		}

		writer.close()

		searcher = new IndexSearcher(directory)

		// 사용할 검색어 질의
		def bq = new BooleanQuery()
		bq.add(new TermQuery(new Term("owner", "bob")), false, false)
		bq.add(new TermQuery(new Term("owner", "sue")), false, false)
		query = bq

		def pastTheEnd = new Date().getTime()//parseDate("2003 Jan 1")
		dateFilter = org.apache.lucene.search.DateFilter.Before("date", 
			pastTheEnd)

		bobFilter = new QueryFilter(new TermQuery(new Term("owner", "bob")))
		sueFilter = new QueryFilter(new TermQuery(new Term("owner", "sue")))
	}
	
	@Test
	public void testSingleFilter() {
	}
}
