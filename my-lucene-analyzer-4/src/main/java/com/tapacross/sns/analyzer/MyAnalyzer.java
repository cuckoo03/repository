package com.tapacross.sns.analyzer;

import java.io.Reader;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer.ReuseStrategy;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;

/**
 * 엘라스틱서치에서 색인시 사용하는 분석기
 * 텍스트가 입력될 때마다 해당 텍스트를 사전서버에서 검색하여 토큰목록을 MyTokenizer에
 * 넘겨주어야 정상동작 한다.
 * @author admin
 *
 */
public class MyAnalyzer extends Analyzer {
	public MyAnalyzer() {
		super(new MyGlobalReuseStrategy());
	}

	Logger log = Logger.getLogger(this.getClass().getCanonicalName());
	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		log.info("createCompoents" + this.hashCode());
		return new TokenStreamComponents(new MyTokenizer(reader));
	}
}