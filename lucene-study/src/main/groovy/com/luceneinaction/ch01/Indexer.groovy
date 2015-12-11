package com.luceneinaction.ch01

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter

/**
 * 디렉토리를 살펴보고 .txt로 끝나는 파일을 읽어들여 색인한다.
 * @author cuckoo03
 *
 */
@TypeChecked
class Indexer {
	static main(args) {
		List<String> argList = (List) args.collect()
		argList.add("./target")
		argList.add("./src/main/groovy/com/luceneinaction/ch01")
		if (argList.size() != 2)
			throw new Exception("Usage: java " + Indexer.class.getName() +
			"<index dir> <data dir>")

		// 루씬 색인을 만든다
		def indexDir = new File(argList.get(0))
		// 이 디렉토리에 포함된 텍스트 파일을 색인한다
		def dataDir = new File(argList.get(1))

		def start = new Date().getTime()
		def numIndexed = index(indexDir, dataDir)
		def end = new Date().getTime()

		println "indexing $numIndexed files took " + (end - start) + " msec"
	}

	/**
	 * 색인을 만들고 파일을 찾아 색인에 읽어들인다
	 */
	public static int index(File indexDir, File dataDir) throws IOException {
		if (!dataDir.exists() || !dataDir.isDirectory())  {
			throw new IOException(dataDir.toString() +
			" does not exist or not a dir")
		}

		// 새로운 루씬 색인을 만든다
		IndexWriter writer = new IndexWriter(indexDir, new StandardAnalyzer(),
				true)
		writer.setUseCompoundFile(false)

		indexDirectory(writer, dataDir)

		def numIndexed = writer.docCount()
		writer.optimize()
		writer.close()
		return numIndexed
	}

	/**
	 * 디렉토리 내부의 텍스트 파일을 모두 읽어들이도록 재귀 호출 방법으로 작성한다
	 */
	private static void indexDirectory(IndexWriter writer, File dir) {
		def files = dir.listFiles()

		for (File f : files) {
			if (f.isDirectory())
				indexDirectory(writer, f)
			else if (f.getName().endsWith(".txt"))
				indexFile(writer, f)
		}
	}

	/**
	 * 루씬을 직접 호출해 파일의 내용을 색인한다
	 * @param writer
	 * @param f
	 */
	private static void indexFile(IndexWriter writer, File f) {
		if (f.isHidden() || !f.exists() || !f.canRead())
			return

		println "indexing " + f.getCanonicalPath()

		Document doc = new Document()
		//파일 본문을 추가한다
		doc.add(Field.Text("contents", new FileReader(f).readLine()))

		//파일 이름을 추가한다
		doc.add(Field.Keyword("filename", f.getCanonicalPath()))
		//파일 1건에 대해 본문과 파일 이름을 색인에 추가한다
		writer.addDocument(doc)
	}
}
