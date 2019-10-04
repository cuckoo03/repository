package com.elasticsearch.index.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.settings.IndexSettings;

import com.tapacross.sns.analyzer.OccasionTokenizer;
import com.tapacross.sns.analyzer.SentimentTokenizer;
import com.tapacross.sns.analyzer.TopicTokenizer;

public class OccasionTokenizerFactory extends AbstractTokenizerFactory {
	@Inject
	public OccasionTokenizerFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
	}

	public Tokenizer create() {
        return new OccasionTokenizer();
    }
}