package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.analysis.StopAnalyzer
import org.apache.lucene.analysis.WhitespaceAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer

/**
 * 분석기가 동작하는 모습 확인
 * @author cuckoo03
 *
 */
@TypeChecked
class AnalyzerDemo {
	private static final String[] examples = [
		"The quick brown fox jumped over the lazy dogs",
		"XY&Z corporation - xyz@example.com",
		"무궁화 꽃이 피었습니다."
	]

	private static final Analyzer[] analyzers = [
		new WhitespaceAnalyzer(),
		new SimpleAnalyzer(),
		new StopAnalyzer(),
		new StandardAnalyzer()
	]

	static main(args) {
		for (int i = 0; i < examples.size(); i++) {
			analyze(examples[i])
		}
	}

	private static void analyze(String text) {
		println "Analyzing \" $text \""
		for (int i = 0; i < analyzers.length; i++) {
			def analyzer = analyzers[i]
			def name = analyzer.getClass().getName()
			name = name.substring(name.lastIndexOf(".") + 1)
			println " $name:"
			AnalyzerUtils.displayTokens(analyzer, text)
//			AnalyzerUtils.displayTokenWithFullDetails(analyzer, text)
			println "\n"
		}
	}
}
