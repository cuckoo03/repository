package com.tapacross.sns.analyzer

import java.io.IOException
import java.io.Reader
import java.io.StringReader
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.analysis.core.WhitespaceAnalyzer
import org.apache.lucene.analysis.core.WhitespaceTokenizer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version

import groovy.transform.TypeChecked

@TypeChecked
class MyAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		final def tokenizer = new MyTokenizer(reader)  
		final def filter = new MyTokenFilter(Version.LUCENE_46, tokenizer)
		return new TokenStreamComponents(tokenizer, filter)
		
//		return new TokenStreamComponents(new MyTokenizer(reader));
//		return new TokenStreamComponents(new WhitespaceAnalyzer(reader2));
//		return new TokenStreamComponents(new MyTokenizerOK(reader2, s));
	}
	
	private String readerToString(Reader reader) throws IOException {
		char[] buffer = new char[4096];
		int charsRead = reader.read(buffer);
		String text = new String(buffer, 0, charsRead);
		return text;
	}
}
