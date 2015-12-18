package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.LowerCaseFilter
import org.apache.lucene.analysis.StopFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.standard.StandardTokenizer

@TypeChecked
class SynonymPhraseStopWordAnalyzer extends Analyzer {
	private SynonymsCache synonymsCache
	private PhrasesCache phrasesCache

	public SynonymPhraseStopWordAnalyzer(SynonymsCache synonymsCache,
	PhrasesCache phrasesCache) {
		this.synonymsCache = synonymsCache
		this.phrasesCache = phrasesCache
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		def tokenizer = new StandardTokenizer(reader)
		def lowerCaseFilter = new LowerCaseFilter(tokenizer)
		def stopFilter = new StopFilter(lowerCaseFilter,
				PorterStemStopWordAnalyzer.stopWords)

		return new SynonymPhraseStopWordFilter(stopFilter, synonymsCache,
				phrasesCache)
	}
}
