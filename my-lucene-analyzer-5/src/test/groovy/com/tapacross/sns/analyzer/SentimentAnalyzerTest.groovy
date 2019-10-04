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
class SentimentAnalyzerTest {
	/**
	 * @throws IOException
	 */
	@Test
	public void testAnalyze() throws IOException {
		String text = "좋은#트와이스 #멜론 #멜론이벤트 트와이스 필스페셜 너무좋다ㅜㅜ 꼭 1위가쟈!!".toLowerCase()
		def analyzer = new SentimentAnalyzer()
		TokenStream stream = analyzer.tokenStream("f", new StringReader(text))
		stream.reset()
		printTerms(stream)
		stream.close()
		System.out.println("------------");

		analyzer.close()
	}

	private void printTerms(TokenStream stream) throws IOException {
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
		TypeAttribute type = stream.addAttribute(TypeAttribute.class);
		PayloadAttribute payload = stream.addAttribute(PayloadAttribute.class);
		int position = 0;

		while (stream.incrementToken()) {
			int increment = posIncr.getPositionIncrement();
			if (increment > 0) {
				position = position + increment;
				System.out.print(position + ":");
			}

			System.out
					.print(term.toString() + " " + offset.startOffset() + 
						"->" + offset.endOffset() + " type:" + type.type() +
						" payload:" + payload.payload.utf8ToString()
						);
			System.out.println();
			System.out.println();
		}
	}
}