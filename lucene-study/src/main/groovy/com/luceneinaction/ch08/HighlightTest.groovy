package com.luceneinaction.ch08

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.highlight.Highlighter
import org.apache.lucene.search.highlight.QueryScorer
import org.junit.Assert
import org.junit.Test

import com.lucene.LiaTestCase

@TypeChecked
class HighlightTest extends LiaTestCase {
	@Test
	public void testHighlighting() {
		def text = "The quick brown fox jumps over the lazy dog"

		def query = new TermQuery(new Term("field", "fox"))
		def scorer = new QueryScorer(query)
		def highlighter = new Highlighter(scorer)

		def tokenStream = new SimpleAnalyzer().
				tokenStream("field", new StringReader(text))

		Assert.assertEquals("The quick brown <B>fox</B> jumps over the lazy dog",
				highlighter.getBestFragment(tokenStream, text))
	}

	@Test
	public void testHits() {
		indexData()
		
		def searcher = new IndexSearcher(directory)

		def query = new TermQuery(new Term("title", "action"))
		def hits = searcher.search(query)

		def scorer = new QueryScorer(query)
		def highliter = new Highlighter(scorer)

		println hits.length()
		
		for (int i = 0; i < hits.length(); i++) {
			def title = hits.doc(i).get("title")

			def stream = new SimpleAnalyzer().
					tokenStream("title", new StringReader(title))
			def fragment = highliter.getBestFragment(stream, title)

			println fragment
		}
	}
	
	private void indexData() {
		// 새로운 루씬 색인을 만든다
		def writer = new IndexWriter(indexDir, new StandardAnalyzer(),
				true)
		writer.setUseCompoundFile(false)
		
		def doc = new Document()
		doc.add(Field.Text("title", "junit in action"))
		writer.addDocument(doc)
		writer.close()
	}
}
