package com.tapacross.sns.analyzer

import groovy.transform.TypeChecked

import java.io.IOException
import java.io.StringReader

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.analysis.core.WhitespaceAnalyzer
import org.apache.lucene.analysis.ngram.NGramTokenizer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute
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
	/**
	 * 형태소분석 실패시: 방탄소년단rt1 0->8 ON
	 * @throws IOException
	 */
	@Test
	public void testAnalyze() throws IOException {
//		String text = "1방 탄 소 년 단 R T 1".toLowerCase()
//		String text = "RT우리짐건 입금을 시작합니다! !?"
		final def text = "ㅋㅋㅋ불치병입니다ㅋㅋㅋㅋㅋㅋㅋㅋㅋ"

		def analyzer = new MyAnalyzer()
		TokenStream stream = analyzer.tokenStream("f", new StringReader(text))
		// http://www.hankcs.com/program/java/lucene-4-6-1-java-lang-illegalstateexception-tokenstream-contract-violation.html
		stream.reset()
		printTerms(stream)
		stream.close()
		System.out.println("------------");

//		text = "한글 이상b"
//		stream = analyzer.tokenStream("f", new StringReader(text))
//		stream.reset()
//		printTerms(stream)
//		stream.close()

		// parse 3
		/*
		text = "한글 이상c"
		stream = analyzer.tokenStream("f", new StringReader(text))
		stream.reset()
		printTerms(stream)
		stream.close()
		*/
		
//		repeat(analyzer, text)

		analyzer.close()
	}

	@Test
	@Ignore
	public void testIndex() throws IOException {
		String text = "버튼을 이용하여 분석식에"
		
		def analyzer = new MyAnalyzer()
		
		Directory dir = new RAMDirectory()
		IndexWriterConfig config = new IndexWriterConfig(analyzer)
		IndexWriter indexWriter = new IndexWriter(dir, config)

		Document doc = new Document()
		def stringField = new TextField("name", "", Field.Store.YES)
		stringField.setStringValue(text)
		doc.add(stringField)
		indexWriter.addDocument(doc)

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
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
		TypeAttribute type = stream.addAttribute(TypeAttribute.class);
		PayloadAttribute payload = stream.addAttribute(PayloadAttribute.class);
		int position = 0;

		while (stream.incrementToken()) {
//			int increment = posIncr.getPositionIncrement();
//			if (increment > 0) {
//				position = position + increment;
//				System.out.print(position + ": ");
//			}

			System.out
					.print(posIncr.getPositionIncrement() + " " + 
						term.toString() + " " + offset.startOffset() + 
						"->" + offset.endOffset() + " type:" + type.type() +
						" payload:" + payload.payload.utf8ToString()
						);
			System.out.println();
			System.out.println();
		}
	}
}
