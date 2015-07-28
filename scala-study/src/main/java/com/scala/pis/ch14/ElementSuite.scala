package com.scala.pis.ch14

import junit.framework.Test
import junit.framework.TestCase
import junit.framework.Assert

/**
 * @author cuckoo03
 */
class ElementSuite extends TestCase {
	def test() {
		Assert.assertEquals(1, 1)
		val s = "test"
		println(s)
	}
}