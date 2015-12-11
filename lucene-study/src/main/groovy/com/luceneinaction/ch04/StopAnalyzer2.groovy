package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.LetterTokenizer
import org.apache.lucene.analysis.LowerCaseFilter
import org.apache.lucene.analysis.StopAnalyzer
import org.apache.lucene.analysis.StopFilter
import org.apache.lucene.analysis.TokenStream

/**
 * StopAnalyzer와 순서를 다르게 불용어를 먼저 제거하고 소문자로 변경하도록 순서를 바꾼 분석기
 * @author cuckoo03
 *
 */
@TypeChecked
class StopAnalyzer2 extends Analyzer {
	private Set stopWords

	public StopAnalyzer2() {
		stopWords = StopFilter.makeStopSet(StopAnalyzer.ENGLISH_STOP_WORDS)
	}

	public StopAnalyzer2(String[] stopWords) {
		this.stopWords = StopFilter.makeStopSet(stopWords)
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new StopFilter(new LowerCaseFilter(
				new LetterTokenizer(reader)), stopWords)
	}
}
