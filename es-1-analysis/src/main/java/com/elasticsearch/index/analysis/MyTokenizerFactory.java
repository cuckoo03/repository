package com.elasticsearch.index.analysis;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.settings.IndexSettings;

import com.tapacross.sns.analyzer.MyTokenizer;
import com.tapacross.sns.analyzer.MyTokenizer2;
import com.tapacross.sns.analyzer.MyTokenizerOK;

public class MyTokenizerFactory extends AbstractTokenizerFactory {
	@Inject
	public MyTokenizerFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
	}

	@Override
    public Tokenizer create(Reader reader) {
		String s = "";
		Reader reader2 = null;
		try {
			s = readerToString(reader);
			reader2 = new StringReader(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// reader2의 문제가 아니라 토크나이저 내부의 특정 코드 문제
        return new MyTokenizer(reader2, s);
//        return new WhitespaceTokenizer(reader);
//        return new MyTokenizerOK(reader2, s);
    }
	
	private String readerToString(Reader reader) throws IOException {
		char[] buffer = new char[4096];
		int charsRead = reader.read(buffer);
		String text = new String(buffer, 0, charsRead);
		return text;
	}
}