package com.luceneinaction.ch07

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.FSDirectory

@TypeChecked
class FileIndexer {
	protected FileHandler fileHandler

	public FileIndexer() {}
	public FileIndexer(Properties props) throws IOException {
		this.fileHandler = new ExtensionFileHandler(props)
	}

	public void index(IndexWriter writer, File file) throws FileHandlerException {
		if (file.canRead()) {
			if (file.isDirectory()) {
				def files = file.list()
				if (files != null) {
					for (int i = 0; i < files.size(); i++) {
						index(writer, new File(file, files[i]))
					}
				}
			}
		} else {
			println "Indexing $file"
			def doc = fileHandler.getDocument(file)
			try {
				if (doc != null) {
					writer.addDocument(doc)
				} else {
					println "Cannot handle ;" + file.getAbsolutePath() + "; skipping"
				}
			} catch (IOException e) {
				println "cannot index " + file.getAbsolutePath() + "; skipping (" + e.getMessage()
			}
		}
	}

	static main(args) {
		/*
		val props = new Properties()
		def propFile = getClass().getClassLoader().
				getResource("com/luceneinaction/ch07/properties").getFile()
		props.load(new FileInputStream(propFile))

		def file = getClass().getClassLoader().
				getResource("com/luceneinaction/ch07/directory").getFile()
		def dir = FSDirectory.getDirectory(file, true)
		def analyzer = new SimpleAnalyzer()
		def writer = new IndexWriter(dir, analyzer, true)

		def indexer = new FileIndexer(props)

		def start = new Date().getTime()
		def indexFile = getClass().getClassLoader().
				getResource("com/luceneinaction/ch07/index").getFile()
		indexer.index(writer, indexFile as File)
		writer.optimize()
		writer.close()
		def end = new Date().getTime()

		println ""
		def reader = IndexReader.open(dir)
		println "Documents indexed:" + reader.numDocs()
		println "total time:" + (end - start) + " ms"
		reader.close()
		*/
	}
}
