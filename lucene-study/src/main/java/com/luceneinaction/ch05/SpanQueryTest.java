package com.luceneinaction.ch05;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.luceneinaction.ch04.AnalyzerUtils;

public class SpanQueryTest {
	private RAMDirectory directory;
	private IndexSearcher searcher;
	private IndexReader reader;

	private SpanTermQuery quick;
	private SpanTermQuery brown;
	private SpanTermQuery red;
	private SpanTermQuery fox;
	private SpanTermQuery lazy;
	private SpanTermQuery sleepy;
	private SpanTermQuery dog;
	private SpanTermQuery cat;
	private Analyzer analyzer;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws IOException {
		directory = new RAMDirectory();

		analyzer = new WhitespaceAnalyzer();
		IndexWriter writer = new IndexWriter(directory, analyzer, true);
		Document doc = new Document();
		doc.add(Field.Text("f", "the quick brown fox jumps over the lazy dog"));
		writer.addDocument(doc);

		doc = new Document();
		doc.add(Field.Text("f", "the quick red fox jumps over the sleepy cat"));

		writer.close();

		searcher = new IndexSearcher(directory);
		reader = IndexReader.open(directory);

		quick = new SpanTermQuery(new Term("f", "quick"));
		brown = new SpanTermQuery(new Term("f", "brown"));
		red = new SpanTermQuery(new Term("f", "red"));
		fox = new SpanTermQuery(new Term("f", "fox"));
		lazy = new SpanTermQuery(new Term("f", "lazy"));
		sleepy = new SpanTermQuery(new Term("f", "sleepy"));
		dog = new SpanTermQuery(new Term("f", "dog"));
		cat = new SpanTermQuery(new Term("f", "cat"));
	}

	private void assertOnlyBrownFox(Query query) throws IOException {
		Hits hits = searcher.search(query);
		Assert.assertEquals(1, hits.length());
		Assert.assertEquals("wrong doc", 0, hits.id(0));
	}

	private void assertBothFoxes(Query query) throws IOException {
		Hits hits = searcher.search(query);
		Assert.assertEquals(2, hits.length());
	}

	private void assertNomatches(Query query) throws IOException {
		Hits hits = searcher.search(query);
		Assert.assertEquals(0, hits.length());
	}

	private void dumpSpans(SpanQuery query) throws IOException {
		Spans spans = query.getSpans(reader);
		System.out.println(query + ":");
		int numSpans = 0;

		Hits hits = searcher.search(query);
		float[] scores = new float[2];
		for (int i = 0; i < hits.length(); i++) {
			scores[hits.id(i)] = hits.score(i);
		}

		while (spans.next()) {
			numSpans++;

			int id = spans.doc();
			Document doc = reader.document(id);

			// 코드를 간결하게 작성하기 위해 토큰은 연속적일고 위치는 0에서 시작한다고 가정한다.
			Token[] tokens = AnalyzerUtils.tokensFromAnalysis(analyzer,
					doc.get("f"));
			StringBuffer buffer = new StringBuffer();
			buffer.append(" ");
			for (int i = 0; i < tokens.length; i++) {
				if (i == spans.start())
					buffer.append("<");

				buffer.append(tokens[i].termText());
				if (i + 1 == spans.end())
					buffer.append(">");

				buffer.append(" ");
			}
			buffer.append("(" + scores[id] + ") ");
			System.out.println(buffer);

			// System.out.println(searcher.explain(query, id));
		}

		if (numSpans == 0)
			System.out.println("No spans");

		System.out.println();
	}

	@Test
	public void testSpanTermQuery() throws IOException {
		assertOnlyBrownFox(brown);
		dumpSpans(brown);
		dumpSpans(new SpanTermQuery(new Term("f", "the")));
	}

	@Test
	public void testSpanFirstQuery() throws IOException {
		SpanFirstQuery query = new SpanFirstQuery(brown, 2);
		assertNomatches(query);

		query = new SpanFirstQuery(brown, 3);
		assertOnlyBrownFox(query);
	}

	@Test
	public void testSpanNearQuery() throws IOException {
		SpanQuery[] quickBrownDog = new SpanQuery[] { quick, brown, dog };
		// 세 개의 연속적인 텀에 대해 질의
		SpanNearQuery snq = new SpanNearQuery(quickBrownDog, 0, true);
		assertNomatches(snq);

		snq = new SpanNearQuery(quickBrownDog, 4, true);
		assertNomatches(snq);

		snq = new SpanNearQuery(quickBrownDog, 5, true);
		assertOnlyBrownFox(snq);

		snq = new SpanNearQuery(new SpanQuery[] { lazy, fox }, 3, false);
		assertOnlyBrownFox(snq);

		PhraseQuery pq = new PhraseQuery();
		pq.add(new Term("f", "lazy"));
		pq.add(new Term("f", "fox"));
		pq.setSlop(4);
		assertNomatches(pq);

		pq.setSlop(5);
		assertOnlyBrownFox(pq);
	}

	@Test
	public void testSpanNotQuery() throws IOException {
		SpanNearQuery quickFox = new SpanNearQuery(
				new SpanQuery[] { quick, fox }, 1, true);
		// fail
//		assertBothFoxes(quickFox);
		dumpSpans(quickFox);
		
		SpanNotQuery quickFoxDog = new SpanNotQuery(quickFox, dog);
		// fail
//		assertBothFoxes(quickFoxDog);
		dumpSpans(quickFoxDog);
		
		SpanNotQuery noQuickRedFox = new SpanNotQuery(quickFox, red);
		assertOnlyBrownFox(noQuickRedFox);
		dumpSpans(noQuickRedFox);
	}
	
	// 작성필요
	@Test
	public void testSpanOrQuery() {
		
	}
}
