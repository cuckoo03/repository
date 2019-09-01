package com.elasticsearch.client.index.analysis
import org.elasticsearch.common.inject.Inject
import org.elasticsearch.common.inject.assistedinject.Assisted
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.index.Index
import org.elasticsearch.index.settings.IndexSettings

import com.tapacross.sns.analyzer.MyTokenFilter

import org.apache.lucene.analysis.TokenStream
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory

import groovy.transform.TypeChecked

@TypeChecked
class MyTokenFilterFactory extends AbstractTokenFilterFactory {
	@Inject
	public MyTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings)
	}

	@Override
	public TokenStream create(TokenStream tokenStream) {
		return new MyTokenFilter(tokenStream)
	}
}
