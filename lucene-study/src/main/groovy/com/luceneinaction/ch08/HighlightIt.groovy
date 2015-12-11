package com.luceneinaction.ch08

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.Term
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.highlight.Highlighter
import org.apache.lucene.search.highlight.QueryScorer
import org.apache.lucene.search.highlight.SimpleFragmenter
import org.apache.lucene.search.highlight.SimpleHTMLFormatter

@TypeChecked
class HighlightIt {
	private static final String text =
	"Contrary to popular belief, Lorem Ipsum is" +
	" not simply random text. It has roots in a piece of" +
	" classical Latin literature from 45 BC, making it over" +
	" 2000 years old. Richard McClintock, a Latin professor" +
	" at Hampden-Sydney College in Virginia, looked up one" +
	" of the more obscure Latin words, consectetur, from" +
	" a Lorem Ipsum passage, and going through the cites" +
	" of the word in classical literature, discovered the" +
	" undoubtable source. Lorem Ipsum comes from sections" +
	" 1.10.32 and 1.10.33 of \"de Finibus Bonorum et" +
	" Malorum\" (The Extremes of Good and Evil) by Cicero," +
	" written in 45 BC. This book is a treatise on the" +
	" theory of ethics, very popular during the" +
	" Renaissance. The first line of Lorem Ipsum, \"Lorem" +
	" ipsum dolor sit amet..\", comes from a line in" +
	" section 1.10.32."  // from http://www.lipsum.com/

	static main(args) {

		def collect = args.collect()
		collect.add("result.html")
		def filename = collect.getAt(0) as String

		if (filename == null) {
			println "Usage: HIghlightIt <filenaem>"
			System.exit(-1)
		}

		def query = new TermQuery(new Term("f", "ipsum"))
		def scorer = new QueryScorer(query)
		def formatter = new SimpleHTMLFormatter("<span class=\"highlight\">",
				"</span>")
		def highlighter = new Highlighter(formatter, scorer)
		def fragmenter = new SimpleFragmenter(50)
		highlighter.setTextFragmenter(fragmenter)

		def tokenStream = new StandardAnalyzer().
				tokenStream("f", new StringReader(text))

		def result = highlighter.getBestFragments(tokenStream, text, 5, "...")

		def writer = new FileWriter(filename)
		writer.write("<html>")
		writer.write("<style>\n" +
				".highlight {\n" +
				" background: yellow;\n" +
				"}\n" +
				"</style>")
		writer.write("<body>")
		writer.write(result)
		writer.write("</body></html>")
		writer.close()


	}
}
