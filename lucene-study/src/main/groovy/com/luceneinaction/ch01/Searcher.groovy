package com.luceneinaction.ch01

import groovy.transform.TypeChecked

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.FSDirectory

/**
 * 주어진 검색어를 포함하는 파일을 검색한다
 * @author cuckoo03
 *
 */
@TypeChecked
class Searcher {
	static main(args) {
		List<String> argList = (List) args.collect()
		argList.add("./target")
		argList.add("filename")
		if (argList.size() != 2) {
			println "Usage:java " + Searcher.class.getName() +
					" <index dir>  <query>"
			System.exit(1)
		}

		// indexer가 색인을 저장한 디렉토리
		def indexDir = new File(argList.get(0))
		// 사용자가 지정한 검색어
		def q = argList.get(1)

		if (!indexDir.exists() || !indexDir.isDirectory()) {
			throw new Exception(indexDir.toString() +
			" dos not exist or not a directory")
		}

		search(indexDir, q)
	}
	private static void search(File indexDir, String q) {
		def fsDir = FSDirectory.getDirectory(indexDir, false)
		//색인을 연다
		def is = new IndexSearcher(fsDir)

		//질의 분석
		def query = QueryParser.parse(q, "contents", new StandardAnalyzer())
		def start = new Date().getTime()
		//검색한다
		def hits = is.search(query)
		def end = new Date().getTime()

		println "found " + hits.length() + " document(s) (in " + (end - start) +
				" msec) that matched query:" + q

		for (int i = 0; i < hits.length(); i++) {
			def doc = hits.doc(i)
			//색인에서 한건의 정보를 가져온다
			println doc.get("filename")
			println doc.get("contents")
		}
	}
}