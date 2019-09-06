package com.tapacross.sns.analyzer

import groovy.transform.TypeChecked

import java.io.IOException
import java.io.StringReader

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute
import org.apache.lucene.analysis.tokenattributes.TypeAttribute
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.Term
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.WildcardQuery
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.util.Version
import org.junit.Ignore
import org.junit.Test

@TypeChecked
class MyAnalyzerTest {
	@Test
	public void testAnalyze() throws IOException {
		String text = "버튼을 이용하여 분석식에"

		def analyzer = new MyAnalyzer()
		// parse 1
		TokenStream stream = analyzer.tokenStream("f", new StringReader(text))
		// http://www.hankcs.com/program/java/lucene-4-6-1-java-lang-illegalstateexception-tokenstream-contract-violation.html
//		stream.reset()
		printTerms(stream)
//		stream.close()

		// parse 2
		stream = analyzer.tokenStream("f", new StringReader(text))
//		stream.reset()
		printTerms(stream)
//		stream.close()

		repeat(analyzer, text)

		analyzer.close()
	}

	@Test
	@Ignore
	public void testIndex() throws IOException {
		String text = "버튼을 이용하여 분석식에"
		
		def analyzer = new MyAnalyzer()
		
		Directory dir = new RAMDirectory()
//		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41)
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer)
		IndexWriter indexWriter = new IndexWriter(dir, config)

		Document doc = new Document()
		def stringField = new TextField("name", "", Field.Store.YES)
		stringField.setStringValue(text)
		doc.add(stringField)
		indexWriter.addDocument(doc, analyzer)

		indexWriter.commit()

		IndexReader indexReader = DirectoryReader.open(dir)
		IndexSearcher indexSearcher = new IndexSearcher(indexReader)
		def query = new TermQuery(new Term("name", "버튼"))
		def topDocs = indexSearcher.search(query, 1)
		for (def scoreDoc : topDocs.scoreDocs) {
			doc = indexSearcher.doc(scoreDoc.doc)
			println "find=" + doc.getField("name").stringValue()
		}
	}
	
	private void printTerms(TokenStream stream) throws IOException {
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class)
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class)
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class)
		TypeAttribute type = stream.addAttribute(TypeAttribute.class)
		int position = 0

		while (stream.incrementToken()) {
			int increment = posIncr.getPositionIncrement()
			if (increment > 0) {
				position = position + increment
				System.out.print(position + ": ")
			}

			System.out
					.print(term.toString() + " " + offset.startOffset() + "->" + offset.endOffset() + " " + type.type())
			System.out.println()
			System.out.println()
		}
	}

	private void repeat(Analyzer analyzer, String text) throws IOException {
		for (int i = 0; i < 10000; i++) {
			TokenStream stream = analyzer.tokenStream("f", new StringReader(text))
			stream.reset()
			printTerms(stream)
			stream.close()
		}
	}
}
