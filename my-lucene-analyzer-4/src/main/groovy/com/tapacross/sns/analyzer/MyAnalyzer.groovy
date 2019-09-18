package com.tapacross.sns.analyzer

import java.io.IOException
import java.io.Reader
import java.io.StringReader
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version

import groovy.transform.TypeChecked

@TypeChecked
class MyAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		return new TokenStreamComponents(new MyTokenizer(reader));
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
