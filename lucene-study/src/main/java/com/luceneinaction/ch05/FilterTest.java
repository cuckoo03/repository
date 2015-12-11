package com.luceneinaction.ch05;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.DateFilter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.junit.Test;

import com.lucene.LiaTestCase;

@SuppressWarnings("deprecation")
public class FilterTest extends LiaTestCase {
	private Query allBooks;
	private IndexSearcher searcher;
	private int numAllBooks;

	@Test
	public void setUp() throws Exception {
		super.setUp();

		allBooks = new RangeQuery(new Term("pubmonth", "190001"), new Term(
				"pubmonth", "200512"), true);
		searcher = new IndexSearcher(directory);
		Hits hits = searcher.search(allBooks);
		numAllBooks = hits.length();
	}

	@Test
	public void testDateFilter() throws ParseException, IOException {
		Date jan1 = parseDate("2004 Jan 01");
		Date jan31 = parseDate("2004 Jan 31");
		Date dec31 = parseDate("2004 Dec 31");

		DateFilter filter = new DateFilter("modified", jan1, dec31);

		Hits hits = searcher.search(allBooks, filter);
		Assert.assertEquals("all modified in 2004", numAllBooks, hits.length());

		filter = new DateFilter("modified", jan1, jan31);
		hits = searcher.search(allBooks, filter);
		Assert.assertEquals("none modified in January", 0, hits.length());
	}
}
