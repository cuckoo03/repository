package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.TokenStream
import org.junit.Test

@TypeChecked
class SynonymPhrasesTest {
	@Test
	public void testSynonymPhrases() throws IOException {
		def synonymsCache = new SynonymsCacheImpl()
		def phrasesCache = new PhrasesCacheImpl()
		def analyzer = new SynonymPhraseStopWordAnalyzer(synonymsCache,
				phrasesCache)
		def text = "Collective Intelligence and Web2.0"
		def TokenStream ts = analyzer.tokenStream(null, new StringReader(text))
		def token = ts.next()
		while (token != null) {
			println token.termText()
			token = ts.next()
		}
	}
}
