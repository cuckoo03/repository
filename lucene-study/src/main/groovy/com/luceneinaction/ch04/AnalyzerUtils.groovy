package com.luceneinaction.ch04

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.Token
import org.junit.Assert

/**
 * 분석기 직접 호출
 * @author cuckoo03
 *
 */
@TypeChecked
class AnalyzerUtils {

	public static Token[] tokensFromAnalysis(Analyzer analyzer, String text) {
		// 분석 기능 호출
		def stream = analyzer.tokenStream("contents", new StringReader(text))
		def tokenList = new ArrayList()
		while (true) {
			def token = stream.next()
			if (token == null)
				break

			tokenList.add(token)
		}
		return tokenList.toArray(new Token[0])
	}

	public static void displayTokens(Analyzer analyzer, String text) {
		def tokens = tokensFromAnalysis(analyzer, text)

		for (int i = 0; i < tokens.length; i++) {
			def token = tokens[i]
			print "[$token.termText()]"
		}
	}

	public static void displayTokenWithFullDetails(Analyzer analyzer, String text) {
		def tokens = tokensFromAnalysis(analyzer, text)
		def position = 0

		for (int i = 0; i < tokens.length; i++) {
			def token = tokens[i]
			def increment = token.getPositionIncrement()
			if (increment > 0) {
				position = position + increment
				println ""
				print "$position:"
			}

			print "[$token.termText:$token.startOffset->$token.endOffset:$token.type]"
		}
	}

	public static void assertTokensEqual(Token[] tokens, String[] strings) {
		Assert.assertEquals(strings.length, tokens.length)

		for (int i = 0; i < tokens.length; i++) {
			Assert.assertEquals(strings[i], tokens[i].termText())
		}
	}
	
	public static void displayTokensWithPositions(Analyzer analyzer, String text) {
		def tokens = tokensFromAnalysis(analyzer, text)
		
		def position = 0
		

		tokens.each { it -> 
			def token = (Token) it
			def increment = ((Token)token).getPositionIncrement()
			
			if (increment > 0) {
				position = position + increment
				println ""
				print "$position: "
			}
			
			print "[" + token.termText() + "]"
		}
	}
}