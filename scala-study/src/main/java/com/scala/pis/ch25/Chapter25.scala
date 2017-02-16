package com.scala.pis.ch25

/**
 * @author cuckoo03
 */
object Chapter25 {
	def main(args: Array[String]) {
		builder()
		wireCommonOper()
	}
	def wireCommonOper() = {
		val f = collection.immutable.Map("a" -> 1, "b" -> 2) map { case (x, y) => (y, x) }
		println(f)

		val xs: collection.Iterable[Int] = collection.immutable.List(1, 2, 3)
		println(xs)
		val ys = xs map (x => x * x)
		println(ys)
	}
	def builder() = {
		val buf = new scala.collection.mutable.ArrayBuffer[Int]
		val bldr = buf mapResult (_.toArray)
		println(bldr)
	}
}