package com.elasticsearch.index.analysis;

import java.io.IOException;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettings;

import com.tapacross.sns.analyzer.SentimentAnalyzer;
import com.tapacross.sns.analyzer.TopicAnalyzer;

public class SentimentAnalyzerProvider extends AbstractIndexAnalyzerProvider<SentimentAnalyzer> {
	private final SentimentAnalyzer analyzer;

	@Inject
	public SentimentAnalyzerProvider(Index index, @IndexSettings Settings indexSettings, 
			Environment env, @Assisted String name, @Assisted Settings settings) throws IOException {
		super(index, indexSettings, name, settings);

		analyzer = new SentimentAnalyzer();
	}

	@Override
	public SentimentAnalyzer get() {
		return this.analyzer;
	}
}
