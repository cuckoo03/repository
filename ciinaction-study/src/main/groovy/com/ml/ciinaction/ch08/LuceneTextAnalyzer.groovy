package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer

@TypeChecked
class LuceneTextAnalyzer implements TextAnalyzer {
	private TagCache tagCache
	private InverseDocFreqEstimator inverseDocFreqEstimator

	public LuceneTextAnalyzer(TagCache tagCache,
	InverseDocFreqEstimator inverseDocFreqEstimator) {
		this.tagCache = tagCache
		this.inverseDocFreqEstimator = inverseDocFreqEstimator
	}

	@Override
	public List<Tag> analyzeText(String text) throws IOException {
		def reader = new StringReader(text)
		def analyzer = getAnalyzer()
		def tags = new ArrayList<Tag>()
		def tokenStream = analyzer.tokenStream(null, reader)
		def token = tokenStream.next()
		while (token != null) {
			tags.add(getTag(token.termText()))
			token = tokenStream.next()
		}

		return tags
	}

	@Override
	public TagMagnitudeVector createTagMagnitudeVector(String text)
	throws IOException {
		// 태그를 생성하기 위한 텍스트 분석
		def tagList = analyzeText(text)
		// 텀 빈도수 계산
		def tagFreqMap = computeTermFrequency(tagList)
		// 역문헌 빈도수 이용
		return applyIDF(tagFreqMap)
	}

	private Tag getTag(String text) throws IOException {
		return this.tagCache.getTag(text)
	}

	private Analyzer getAnalyzer() throws IOException {
		return new SynonymPhraseStopWordAnalyzer(new SynonymsCacheImpl(),
				new PhrasesCacheImpl())
	}

	private Map<Tag, Integer> computeTermFrequency(List<Tag> tagList) {
		def tagFreqMap = new HashMap<Tag, Integer>()
		tagList.each { tag ->
			def count = tagFreqMap.get(tag)
			if (count == null)
				count = new Integer(1)
			else
				count = new Integer(count.plus(1))

			tagFreqMap.put(tag, count)
		}

		return tagFreqMap
	}

	private TagMagnitudeVector applyIDF(Map<Tag, Integer> tagFreqMap) {
		def tagMagnitudes = new ArrayList<TagMagnitude>()
		tagFreqMap.keySet().each { tag ->
			def idf = this.inverseDocFreqEstimator.estimateInverseDocFreq(tag)
			def tf = tagFreqMap.get(tag)
			def wt = tf * idf
			tagMagnitudes.add(new TagMagnitudeImpl(tag, wt))
		}

		return new TagMagnitudeVectorImpl(tagMagnitudes)
	}

	private void displayTextAnalysis(String text) throws IOException {
		def tags = analyzeText(text)
		tags.each { tag -> println  tag }
	}

	static main(args) {
		def title = "Collective Intelligence and Web2.0"
		def body = "Web2.0 is all about connecting users to users, " +
				" inviting users to participate and applying their " +
				" collective intelligence to improve the application. " +
				" Collective intelligence" +
				" enhances the user experience"

		def tagCache = new TagCacheImpl()
		def idfEstimator = new EqualInverseDocFreqEstimator()
		def lta = new LuceneTextAnalyzer(tagCache, idfEstimator)
		
		println "Analyzing the title"
		lta.displayTextAnalysis(title)

		println "Analyzing the body"
		lta.displayTextAnalysis(body)
		
	}
}
