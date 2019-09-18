package com.elasticsearch.index.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.settings.IndexSettings;

import com.tapacross.sns.analyzer.MyTokenizer;

public class MyTokenizerFactory extends AbstractTokenizerFactory {
	@Inject
	public MyTokenizerFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
	}

	@Override
    public Tokenizer create(Reader reader) {
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
		// reader2의 문제가 아니라 토크나이저 내부의 특정 코드 문제
        return new MyTokenizer(reader);
//		return new MyCharTokenizer(Version.LUCENE_46, reader2, s);
    }
}