package com.tapacross.sns.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Test;

import com.tapacross.sns.analyzer.MyAnalyzer;

/**
 * Unit test for simple App.
 */
public class AppTest {
	@Test
	public void test() throws IOException {
		String[] array = new String[]{"버튼 이용 1", "버튼 사용2", "버튼 유즈3"};
		Analyzer analyzer = new MyAnalyzer();
		// parse 1
		TokenStream stream = null;
		for (String text : array) {
			stream = analyzer.tokenStream("f", new StringReader(text));
			// http://www.hankcs.com/program/java/lucene-4-6-1-java-lang-illegalstateexception-tokenstream-contract-violation.html
			stream.reset();
			printTerms(stream);
			System.out.println("------------");
		}
		analyzer.close();
	}
	
	private void printTerms(TokenStream stream) throws IOException {
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
		TypeAttribute type = stream.addAttribute(TypeAttribute.class);
		int position = 0;

		while (stream.incrementToken()) {
			int increment = posIncr.getPositionIncrement();
			if (increment > 0) {
				position = position + increment;
				System.out.print(position + ": ");
			}

			System.out
					.print(term.toString() + " " + offset.startOffset() + "->" + offset.endOffset() + " " + type.type());
			System.out.println();
			System.out.println();
		}
	}
}
