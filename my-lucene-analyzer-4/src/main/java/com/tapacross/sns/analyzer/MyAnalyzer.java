package com.tapacross.sns.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.util.Version;

/**
 * 엘라스틱서치에서 색인시 사용하는 분석기
 * 텍스트가 입력될 때마다 해당 텍스트를 사전서버에서 검색하여 토큰목록을 MyTokenizer에
 * 넘겨주어야 정상동작 한다.
 * @author admin
 *
 */
public class MyAnalyzer extends Analyzer {
	public MyAnalyzer() {
//		super(new MyGlobalReuseStrategy());
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		/*
		String s = "";
		Reader reader2 = null;
		try {
			s = readerToString(reader);
			reader2 = new StringReader(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
//		return new TokenStreamComponents(new MyTokenizer(reader2, s));
//		return new TokenStreamComponents(new WhitespaceTokenizer(reader));
		return new TokenStreamComponents(new MyTokenizerOK(reader));
	}
	
	private String readerToString(Reader reader) throws IOException {
		char[] buffer = new char[4096];
		int charsRead = reader.read(buffer);
		String text = new String(buffer, 0, charsRead);
		return text;
	}
}