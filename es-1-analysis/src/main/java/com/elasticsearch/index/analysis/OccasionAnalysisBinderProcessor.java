package com.elasticsearch.index.analysis;

import org.elasticsearch.index.analysis.AnalysisModule;

public class OccasionAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {
	@Override
	public void processAnalyzers(AnalyzersBindings analyzersBindings) {
		analyzersBindings.processAnalyzer("occasion_analyzer", OccasionAnalyzerProvider.class);
	}

	@Override
	public void processTokenizers(TokenizersBindings tokenizersBindings) {
		tokenizersBindings.processTokenizer("occasion_tokenizer", OccasionTokenizerFactory.class);
	}

	@Override
	public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
		tokenFiltersBindings.processTokenFilter("occasion_filter", OccasionTokenFilterFactory.class);
	}
}
