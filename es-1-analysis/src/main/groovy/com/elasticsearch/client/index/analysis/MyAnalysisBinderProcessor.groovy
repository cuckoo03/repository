package com.elasticsearch.client.index.analysis

import org.elasticsearch.index.analysis.AnalysisModule
import org.elasticsearch.index.analysis.AnalysisModule.AnalysisBinderProcessor.AnalyzersBindings
import org.elasticsearch.index.analysis.AnalysisModule.AnalysisBinderProcessor.TokenFiltersBindings
import org.elasticsearch.index.analysis.AnalysisModule.AnalysisBinderProcessor.TokenizersBindings

import groovy.transform.TypeChecked

@TypeChecked
class MyAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {
	@Override
	public void processAnalyzers(AnalyzersBindings analyzersBindings) {
		analyzersBindings.processAnalyzer("my_analyzer", MyAnalyzerProvider.class);
	}

	@Override
	public void processTokenizers(TokenizersBindings tokenizersBindings) {
		tokenizersBindings.processTokenizer("my_tokenizer", MyTokenizerFactory.class);
	}

	@Override
	public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
		tokenFiltersBindings.processTokenFilter("my_filter", MyTokenFilterFactory.class);
	}
}
