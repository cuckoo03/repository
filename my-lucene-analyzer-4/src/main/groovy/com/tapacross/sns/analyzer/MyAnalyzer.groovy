package com.tapacross.sns.analyzer

import java.io.IOException
import java.io.Reader
import java.io.StringReader
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents
import org.apache.lucene.analysis.CharFilter
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter
import org.apache.lucene.analysis.core.WhitespaceAnalyzer
import org.apache.lucene.analysis.core.WhitespaceTokenizer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version

import groovy.transform.TypeChecked

/**
 * 형태소 분석기
 * @author admin
 *
 */
@TypeChecked
class MyAnalyzer extends Analyzer {
	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		final def tokenizer = new MyTokenizer(reader)  
		final def filter = new MyTokenFilter(Version.LUCENE_46, tokenizer)
		return new TokenStreamComponents(tokenizer, filter)
	}
}
