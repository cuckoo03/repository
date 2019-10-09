package com.elasticsearch.index.analysis;

import java.io.IOException;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexDynamicSettings;

import com.tapacross.sns.analyzer.MyAnalyzer;

public class MyAnalyzerProvider extends AbstractIndexAnalyzerProvider<MyAnalyzer> {
	private final MyAnalyzer analyzer;

	@Inject
	public MyAnalyzerProvider(Index index, @IndexDynamicSettings Settings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) throws IOException {
		super(index, indexSettings, name, settings);

		analyzer = new MyAnalyzer();
	}

	public MyAnalyzer get() {
		return this.analyzer;
	}
}
