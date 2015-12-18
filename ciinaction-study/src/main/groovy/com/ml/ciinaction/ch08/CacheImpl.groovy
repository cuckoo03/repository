package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer

@TypeChecked
class CacheImpl {
	private Analyzer stemmer
	public CacheImpl() {
		this.stemmer = new PorterStemStopWordAnalyzer()
	}

	protected String getStemmedText(String text) throws IOException {
		def sb = new StringBuilder()
		def reader = new StringReader(text)
		def tokenStream = this.stemmer.tokenStream(null, reader)

		def token = tokenStream.next()
		while (token != null) {
			sb.append(token.termText())
			token = tokenStream.next()
			if (token != null)
				sb.append(" ")
		}
		return sb.toString()
	}
}
