package com.luceneinaction.ch02

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.FSDirectory

@TypeChecked
class IndexTuningDemo {
	static main(args) {
		List<String> argList = (List) args.collect()
		argList.add(0, "10000000000000000")
		argList.add(1, "10")
		argList.add(2, "999999999")
		argList.add(3, "100")

		def docsInIndex = argList.get(0)

		def dir = FSDirectory.getDirectory(
				System.getProperty("java.io.tmpdir", "tmp") +
				System.getProperty("file.separator") + "index_dir", true)
		def analyzer = new SimpleAnalyzer()
		def writer = new IndexWriter(dir, analyzer, true)

		writer.mergeFactor = Integer.parseInt(argList.get(1))
		writer.maxMergeDocs = Integer.parseInt(argList.get(2))
		writer.minMergeDocs = Integer.parseInt(argList.get(3))
		writer.infoStream = System.out

		println "Merge factor:$writer.mergeFactor"
		println "Max merge docs:$writer.maxMergeDocs"
		println "Min merge docs:$writer.minMergeDocs"

		def start = System.currentTimeMillis()
		docsInIndex.each { it ->
			def doc = new Document()
			doc.add(Field.Text("fieldname", "Bibamus"))
			writer.addDocument(doc)
		}
		writer.close()
		def stop = System.currentTimeMillis()
		println "Time:" + (stop - start) + " ms"
	}
}
