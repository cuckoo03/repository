package com.tapacross.sns.analyzer

import java.io.IOException

import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.core.StopFilter
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.TypeAttribute
import org.apache.lucene.analysis.util.FilteringTokenFilter
import org.apache.lucene.util.Version

import groovy.transform.TypeChecked

/**
 * TPO 필터
 * @author admin
 *
 */
@TypeChecked
class OccasionTokenFilter extends FilteringTokenFilter {
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	
	/**
	 * Create a new {@link FilteringTokenFilter}.
	 * @param version the Lucene match version
	 * @param in      the {@link TokenStream} to consume
	 */
	public OccasionTokenFilter(Version version, TokenStream stream) {
		super(version, stream);
	}

	/** Override this method and return if the current input token should be returned by {@link #incrementToken}. */
	@Override
	protected boolean accept() throws IOException {
		return true 
	}

	@Override
	public void reset() throws IOException {
		super.reset();
	}

	/**
	 * @see #setEnablePositionIncrements(boolean)
	 */
	@Override
	public boolean getEnablePositionIncrements() {
		return enablePositionIncrements;
	}

	@Override
	public void end() throws IOException {
		super.end();
	}
}
