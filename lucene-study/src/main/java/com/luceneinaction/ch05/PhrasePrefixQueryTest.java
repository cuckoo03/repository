package com.luceneinaction.ch05;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhrasePrefixQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

public class PhrasePrefixQueryTest {
	private IndexSearcher searcher;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws IOException {
		RAMDirectory dir = new RAMDirectory();
		IndexWriter writer = new IndexWriter(dir, new WhitespaceAnalyzer(),
				true);
		Document doc1 = new Document();
		doc1.add(Field.Text("field",
				"the quick brown fox jumped over the lazy dog"));
		writer.addDocument(doc1);

		Document doc2 = new Document();
		doc2.add(Field.Text("field", "the fast fox hopped over the hound"));
		writer.addDocument(doc2);
		writer.close();

		searcher = new IndexSearcher(dir);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testBasic() throws IOException {
		PhrasePrefixQuery query = new PhrasePrefixQuery();
		query.add(new Term[] { new Term("field", "quick"),
				new Term("field", "fast") });
		query.add(new Term("field", "fox"));
		Hits hits = searcher.search(query);
		Assert.assertEquals("fast fox match", 1, hits.length());

		query.setSlop(1);
		hits = searcher.search(query);
		Assert.assertEquals("both match", 2, hits.length());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAgainstOR() throws IOException {
		PhraseQuery quickFox = new PhraseQuery();
		quickFox.setSlop(1);
		quickFox.add(new Term("field", "quick"));
		quickFox.add(new Term("field", "fox"));

		PhraseQuery fastFox = new PhraseQuery();
		fastFox.add(new Term("field", "fast"));
		fastFox.add(new Term("field", "fox"));

		BooleanQuery query = new BooleanQuery();
		query.add(quickFox, false, false);
		query.add(fastFox, false, false);
		Hits hits = searcher.search(query);
		Assert.assertEquals(2, hits.length());
	}
}
