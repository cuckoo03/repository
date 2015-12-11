package com.luceneinaction.ch05;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

public class MultiSearcherTest {
	private IndexSearcher[] searchers;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws IOException {
		String[] animals = { "aardvark", "beaver", "coati", "dog", "elephant",
				"frog" };
		Analyzer analyzer = new WhitespaceAnalyzer();

		Directory aTomDirectory = new RAMDirectory();
		Directory nTozDirectory = new RAMDirectory();

		IndexWriter aTomWriter = new IndexWriter(aTomDirectory, analyzer, true);
		IndexWriter nTozWriter = new IndexWriter(nTozDirectory, analyzer, true);

		for (int i = 0; i < animals.length; i++) {
			Document doc = new Document();
			String animal = animals[i];
			doc.add(Field.Keyword("animal", animal));
			if (animal.compareToIgnoreCase("a") < 0)
				aTomWriter.addDocument(doc);
			else
				nTozWriter.addDocument(doc);
		}

		aTomWriter.close();
		nTozWriter.close();

		searchers = new IndexSearcher[2];
		searchers[0] = new IndexSearcher(aTomDirectory);
		searchers[1] = new IndexSearcher(nTozDirectory);
	}

	@Test
	public void testMulti() throws IOException {
		MultiSearcher searcher = new MultiSearcher(searchers);

		Query query = new RangeQuery(new Term("animal", "a"), new Term(
				"animal", "f"), true);

		Hits hits = searcher.search(query);
		Assert.assertEquals(6, hits.length());

	}
}
