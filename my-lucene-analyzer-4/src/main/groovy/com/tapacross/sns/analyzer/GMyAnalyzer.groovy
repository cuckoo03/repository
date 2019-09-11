package com.tapacross.sns.analyzer

import java.io.IOException
import java.io.Reader
import java.io.StringReader
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents

import groovy.transform.TypeChecked

@TypeChecked
class GMyAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		String s = "";
		Reader reader2 = null;
		try {
			s = readerToString(reader);
			reader2 = new StringReader(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new TokenStreamComponents(new MyTokenizer(reader2, s));
//		return new TokenStreamComponents(new WhitespaceTokenizer(reader2));
//		return new TokenStreamComponents(new MyTokenizerOK(reader2, s));
	}
	
	private String readerToString(Reader reader) throws IOException {
		char[] buffer = new char[4096];
		int charsRead = reader.read(buffer);
		String text = new String(buffer, 0, charsRead);
		return text;
	}
}
