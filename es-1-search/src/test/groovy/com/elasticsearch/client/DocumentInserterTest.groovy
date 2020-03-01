package com.elasticsearch.client

import org.junit.Test

import groovy.transform.TypeChecked

@TypeChecked
class DocumentInserterTest {
	@Test
	def void test() {
		def documentInserter = new DocumentInserter()
		def args = new String[4]
		args[0] = 0
		args[1] = "media"
		args[2] = "2002"
		args[3] = 4
		documentInserter.main(args)
		// 4thread 281 sec(twitter일경우)
		// 8thread 159 sec(twitter일경우)
		// 16thread 137 sec(twitter일경우)
	}
}
