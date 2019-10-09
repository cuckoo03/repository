package com.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexDynamicSettings;

import com.tapacross.sns.analyzer.MyTokenFilter;

public class MyTokenFilterFactory extends AbstractTokenFilterFactory {
	@Inject
	public MyTokenFilterFactory(Index index, @IndexDynamicSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
	}

	public TokenStream create(TokenStream tokenStream) {
		return new MyTokenFilter(tokenStream);
	}
}
