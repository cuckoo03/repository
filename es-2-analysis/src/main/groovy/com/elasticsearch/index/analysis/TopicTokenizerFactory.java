package com.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.settings.IndexDynamicSettings;

import com.tapacross.sns.analyzer.TopicTokenizer;

public class TopicTokenizerFactory extends AbstractTokenizerFactory {
	@Inject
	public TopicTokenizerFactory(Index index, @IndexDynamicSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
	}

    public Tokenizer create() {
        return new TopicTokenizer();
    }
}