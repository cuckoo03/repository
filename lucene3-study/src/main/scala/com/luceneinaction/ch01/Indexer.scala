package com.luceneinaction.ch01

import java.io.File
import java.io.FileFilter
import java.io.FileReader
import java.io.IOException

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version

/**
 * @author cuckoo03
 */
class Indexer(indexDir: String) {
	private val dir = FSDirectory.open(new File(indexDir))
	private val writer = new IndexWriter(dir,
		new StandardAnalyzer(Version.LUCENE_30), true,
		IndexWriter.MaxFieldLength.UNLIMITED)

	@throws(classOf[IOException])
	def close() {
		writer.close()
	}

	@throws(classOf[Exception])
	def index(dataDir: String, filter: FileFilter): Int = {
		def files = new File(dataDir).listFiles()

		for (f <- files) {
			if (!f.isDirectory() && f.exists() && !f.isHidden() && f.canRead()
				&& (filter == null || filter.accept(f))) {
				indexFile(f)
			}
		}
		return writer.numDocs()
	}

	protected def getDocument(f: File): Document = {
		val doc = new Document()
		doc.add(new Field("contents", new FileReader(f)))
		doc.add(new Field("filename", f.getName, Field.Store.YES,
			Field.Index.NOT_ANALYZED))
		doc.add(new Field("fullpath", f.getCanonicalPath, Field.Store.YES,
			Field.Index.NOT_ANALYZED))
		return doc
	}

	@throws(classOf[Exception])
	private def indexFile(f: File) {
		println("indexing " + f.getCanonicalPath())
		val doc = getDocument(f)
		writer.addDocument(doc)
	}
}