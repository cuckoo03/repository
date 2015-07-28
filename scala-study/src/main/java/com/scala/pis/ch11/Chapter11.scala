package com.scala.pis.ch11

import java.lang.ref.ReferenceQueue.Null

/**
 * @author cuckoo03
 */
object Chapter11 {
	def main(args: Array[String]) {
		println(42.hashCode())
		println(1 until 5)

		def isEqual(x: Int, y: Int) = x == y
		println(isEqual(1, 1))

		println("a" eq "a")
		
	}
}