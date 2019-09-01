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

/**
 * https://lucene.apache.org/core/4_1_0/core/org/apache/lucene/analysis/package-summary.html
 * https://mvnrepository.com/artifact/org.apache.lucene/lucene-core
 * https://www.cnblogs.com/TerryLiang/archive/2012/10/08/2714918.html
 *
 */
public class AnalyzerTest {
	@Test
	public void test() throws IOException {
		String text = "버튼을 이용하여 분석식에";
		
		String[] tokens = new String[]{"버튼", "을", "이용하", "여", "분석", "식에"};
		String[] pos = new String[]{"NN", "PP", "NN", "XX", "NN", "XX"};		
		
		TapacrossAnalyzer analyzer = new TapacrossAnalyzer();
		analyzer.setTokens(tokens);
		analyzer.setpos(pos);
		// parse 1
		TokenStream stream = analyzer.tokenStream("f", new StringReader(text));
		// http://www.hankcs.com/program/java/lucene-4-6-1-java-lang-illegalstateexception-tokenstream-contract-violation.html
		stream.reset();
		printTerms(stream);
		stream.close();
		
		// parse 2
		stream = analyzer.tokenStream("f", new StringReader(text));
		stream.reset();
		printTerms(stream);
		stream.close();
		
		repeat(analyzer, text);
		
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
	
	private void repeat(Analyzer analyzer, String text) throws IOException {
		for (int i = 0; i < 10000; i++) {
			TokenStream stream = analyzer.tokenStream("f", new StringReader(text));
			stream.reset();
			printTerms(stream);
			stream.close();
		}
	}
}
