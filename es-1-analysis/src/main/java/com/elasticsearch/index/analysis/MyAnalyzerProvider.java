package com.elasticsearch.index.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettings;

import com.tapacross.sns.analyzer.MyAnalyzer;
import com.tapacross.sns.analyzer.MyGlobalReuseStrategy;

public class MyAnalyzerProvider extends AbstractIndexAnalyzerProvider<MyAnalyzer> {
	private final MyAnalyzer analyzer;

	@Inject
	public MyAnalyzerProvider(Index index, @IndexSettings Settings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) throws IOException {
		super(index, indexSettings, name, settings);

		analyzer = new MyAnalyzer();
	}

	@Override
	public MyAnalyzer get() {
		return this.analyzer;
	}
}
