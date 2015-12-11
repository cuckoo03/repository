package com.luceneinaction.ch05

import groovy.transform.TypeChecked

import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.TermFreqVector

import com.lucene.LiaTestCase

@TypeChecked
class CategorizerTest extends LiaTestCase {
	private Map<String, TreeMap> categoryMap

	@Override
	protected void setUp() {
		super.setUp()

		this.categoryMap = new TreeMap<>()

		buildCategoryVectors()
	}

	private void buildCategoryVectors() {
		def reader = IndexReader.open(directory)
		def maxDoc = reader.maxDoc()

		for (int i = 0; i < maxDoc; i++) {
			def doc = reader.document(i)
			String category = doc.get("category")

			def vectorMap = categoryMap.get(category)
			if (vectorMap == null) {
				vectorMap = new TreeMap<String, Integer>()
				categoryMap.put(category, vectorMap)
			}

			def termFreqVector = reader.getTermFreqVector(i, "subject")

			addTermFreqToMap(vectorMap, termFreqVector)
		}
	}

	private void addTermFreqToMap(Map<String, Integer> vectorMap,
			TermFreqVector termFreqVector) {
		def terms = termFreqVector.getTerms()
		def freqs = termFreqVector.getTermFrequencies()

		for (int i = 0; i < terms.size(); i++) {
			def term = terms[i]

			if (vectorMap.containsKey(term)) {
				def value = vectorMap.get(term)
				vectorMap.put(term, new Integer(value.intValue() + freqs[i]))
				vectorMap.put(term, value + freqs[i])
			} else {
				vectorMap.put(term, freqs[i])
			}
		}
	}

	private String getCategory(String subject) {
		def words = subject.split(" ")
		
		def categoryIterator = categoryMap.keySet().iterator()
		def bestAngle = Double.MAX_VALUE
		def bestCategory = null
		
		while (categoryIterator.hasNext()) {
			def category = categoryIterator.next()
			
			def angle = computeAngle(words, category)
			if (angle < bestAngle) {
				bestAngle = angle
				bestCategory = category
			}
		}

		return bestCategory
	}
	
	/**
	 * 작성중
	 * @param words
	 * @param category
	 * @return
	 */
	private double computeAngle(String[] words, String category) {
		return 0L
	}
}
