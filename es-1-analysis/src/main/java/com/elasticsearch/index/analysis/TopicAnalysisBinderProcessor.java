package com.elasticsearch.index.analysis;

import org.elasticsearch.index.analysis.AnalysisModule;

public class TopicAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {
	@Override
	public void processAnalyzers(AnalyzersBindings analyzersBindings) {
		analyzersBindings.processAnalyzer("topic_analyzer", TopicAnalyzerProvider.class);
	}

	@Override
	public void processTokenizers(TokenizersBindings tokenizersBindings) {
		tokenizersBindings.processTokenizer("topic_tokenizer", TopicTokenizerFactory.class);
	}

//	@Override
//	public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
//		tokenFiltersBindings.processTokenFilter("my_filter", MyTokenFilterFactory.class);
//	}
}
