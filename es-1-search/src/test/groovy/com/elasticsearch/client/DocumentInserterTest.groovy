package com.elasticsearch.client

import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class DocumentInserterTest {
	@Test
	def void test() {
		def documentInserter = new DocumentInserter()
		def args = new String[3]
		args[0] = 0
		args[1] = "twitter"
		args[2] = "1910"
		documentInserter.main(args)
	}
}
