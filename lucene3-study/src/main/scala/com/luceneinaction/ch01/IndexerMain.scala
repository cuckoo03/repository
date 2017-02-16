package com.luceneinaction.ch01

/**
 * @author cuckoo03
 */
object IndexerMain {
	def main(args: Array[String]) {
		if (args.size != 2) {
			println("Usage:java <index dir> <data dir>")
			System.exit(1)
		}

		val indexDir = args(0)
		val dataDir = args(1)

		val start = System.currentTimeMillis()
		val indexer = new Indexer(indexDir)

		var numIndexed = 0
		try {
			numIndexed = indexer.index(dataDir, new TextFileFilter())
		} finally {
			indexer.close()
		}

		val end = System.currentTimeMillis()

		println("Indexing " + numIndexed + " files took " + (end - start) + " msec")
	}
}