package com.tapacross.sns.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.ReuseStrategy;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;

public class MyGlobalReuseStrategy extends ReuseStrategy {
	/**
	 * Sole constructor. (For invocation by subclass constructors, typically
	 * implicit.)
	 * 
	 * @deprecated Don't create instances of this class, use
	 *             {@link Analyzer#GLOBAL_REUSE_STRATEGY}
	 */
	@Override
	public TokenStreamComponents getReusableComponents(Analyzer analyzer, String fieldName) {
		return null;
	}

	@Override
	public void setReusableComponents(Analyzer analyzer, String fieldName, TokenStreamComponents components) {
		setStoredValue(analyzer, components);
	}
}
