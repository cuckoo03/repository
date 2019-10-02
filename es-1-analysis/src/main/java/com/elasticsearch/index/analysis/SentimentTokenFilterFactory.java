package com.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;

import com.tapacross.sns.analyzer.SentimentTokenFilter;
import com.tapacross.sns.analyzer.TopicTokenFilter;

public class SentimentTokenFilterFactory extends AbstractTokenFilterFactory {
	@Inject
	public SentimentTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
	}

	@Override
	public TokenStream create(TokenStream tokenStream) {
		return new SentimentTokenFilter(Version.LUCENE_46, tokenStream);
	}
}
