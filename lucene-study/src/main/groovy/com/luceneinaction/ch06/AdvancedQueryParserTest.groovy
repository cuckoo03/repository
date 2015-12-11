package com.luceneinaction.ch06

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.WhitespaceAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.Term
import org.apache.lucene.queryParser.ParseException
import org.apache.lucene.search.Query
import org.apache.lucene.search.RangeQuery
import org.apache.lucene.store.RAMDirectory
import org.junit.Before

import com.lucene.NumberUtils

@TypeChecked
class AdvancedQueryParserTest {
	private Analyzer analyzer
	private RAMDirectory directory

	@Before
	public void setUp() {
		analyzer = new WhitespaceAnalyzer()

		directory = new RAMDirectory()
		def writer = new IndexWriter(directory, analyzer, true)
		1..500.each  { i ->
			def doc = new Document()
			doc.add(Field.Keyword("id", NumberUtils.pad((int)i)))
			writer.addDocument(doc)
		}

		writer.close()
	}

	/**
	 * 작성중
	 * @param field
	 * @param analyzer
	 * @param part1
	 * @param part2
	 * @param inclusive
	 * @return
	 */
	private Query getRangeQuery(String field, Analyzer analyzer,
			String part1, String part2, boolean inclusive) {
		if ("id" == field) {
			try {
				def num1 = Integer.parseInt(part1)
				def num2 = Integer.parseInt(part2)
				return new RangeQuery(
						new Term(field, NumberUtils.pad(num1)),
						new Term(field, NumberUtils.pad(num2)), inclusive)
			} catch (NumberFormatException e) {
				throw new ParseException(e.getMessage())
			}
		}
		return null
	}
}
