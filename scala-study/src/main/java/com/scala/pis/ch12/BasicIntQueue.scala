package com.scala.pis.ch12

import scala.collection.mutable.ArrayBuffer

/**
 * @author cuckoo03
 */
class BasicIntQueue extends IntQueue {
	private val buf = new ArrayBuffer[Int]
	@Override
	def get() = buf.remove(0)
	@Override
	def put(x: Int) {
		buf += x
	}
}