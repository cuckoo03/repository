package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import java.text.ParseException

import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.Hits
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.store.RAMDirectory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@TypeChecked
class PositionalPorterStopAnalyzerTest {
	private static PositionalPorterStopAnalyzer porterAnalyzer = new PositionalPorterStopAnalyzer()
	private RAMDirectory directory

	@Before
	public void setUp() {
		directory = new RAMDirectory()
		def writer = new IndexWriter(directory, porterAnalyzer, true)

		def doc = new Document()
		doc.add(Field.Text("contents",
				"The quick brown fox jumps over the lazy dogs"))
		writer.addDocument(doc)
		writer.close()
	}

	@Test
	public void textExactPhrase() throws IOException, ParseException {
		def searcher = new IndexSearcher(directory)
		Query query = QueryParser.parse("\"over the lazy\"", "contents",
				porterAnalyzer)

		Hits hits = searcher.search(query);
		// fail
		Assert.assertEquals("exact match not found", 0, hits.length());
	}

	@Test
	public void textWithSlop() throws IOException, ParseException {
		def searcher = new IndexSearcher(directory);

		def parser = new QueryParser("contents", porterAnalyzer);
		parser.setPhraseSlop(1);

		def query = parser.parse("\"over the lazy\"", "contents",
				porterAnalyzer);

		def hits = searcher.search(query);
		// fail
		Assert.assertEquals("exact match not found", 1, hits.length());
	}

	@Test
	public void testStem() throws ParseException, IOException {
		def searcher = new IndexSearcher(directory);
		def query = QueryParser.parse("laziness", "contents", porterAnalyzer);
		def hits = searcher.search(query);
		Assert.assertEquals("lazi", 1, hits.length());

		query = QueryParser.parse("\"fox jumped\"", "contents", porterAnalyzer);
		hits = searcher.search(query);
		Assert.assertEquals("jump jumps jumped jumping", 1, hits.length());
	}
}
