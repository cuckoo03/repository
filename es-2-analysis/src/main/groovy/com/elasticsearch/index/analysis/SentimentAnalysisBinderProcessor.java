package com.elasticsearch.index.analysis;

import org.elasticsearch.index.analysis.AnalysisModule;

public class SentimentAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {
	@Override
	public void processAnalyzers(AnalyzersBindings analyzersBindings) {
		analyzersBindings.processAnalyzer("sentiment_analyzer", SentimentAnalyzerProvider.class);
	}

	@Override
	public void processTokenizers(TokenizersBindings tokenizersBindings) {
		tokenizersBindings.processTokenizer("sentiment_tokenizer", SentimentTokenizerFactory.class);
	}

	@Override
	public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
		tokenFiltersBindings.processTokenFilter("sentiment_filter", SentimentTokenFilterFactory.class);
	}
}
