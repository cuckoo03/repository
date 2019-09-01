package com.elasticsearch.client.index.analysis
import java.io.Reader

import org.apache.lucene.analysis.Tokenizer
import org.elasticsearch.common.inject.Inject
import org.elasticsearch.common.inject.assistedinject.Assisted
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.index.Index
import org.elasticsearch.index.settings.IndexSettings


import org.elasticsearch.index.analysis.AbstractTokenizerFactory
import com.tapacross.sns.analyzer.MyTokenizer
import groovy.transform.TypeChecked

@TypeChecked
class MyTokenizerFactory extends AbstractTokenizerFactory {
	@Inject
	public MyTokenizerFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
	}

	@Override
    public Tokenizer create(Reader reader) {
        return new MyTokenizer(reader);
    }
}
