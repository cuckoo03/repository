package com.scala.pis.ch08

import java.io.File

/**
 * @author cuckoo03
 */
object Exam01 {
	def main(args: Array[String]) {
		val newArgs = Array("45", "pom.xml")
		val width = newArgs(0).toInt
		for (arg <- newArgs.drop(1))
			LongLine.processFile(arg, width)

		// first class function
		var increase = (x: Int) => x + 1
		println(increase(1))

		increase = (x: Int) => {
			println("a")
			x - 1
		}
		println("increase=" + increase(1))

		val someNumbers = List(-10, 10)
		someNumbers.foreach { x => println(x) }
		someNumbers.foreach { println _ }

		println(someNumbers.filter((x: Int) => x > 0))
		println(someNumbers.filter(x => { x > 0 }))
		someNumbers.filter { (x: Int) => x > 0 }
		someNumbers.filter { x => { x > 0 } }

		println(someNumbers.filter(_ > 0))

		someNumbers.foreach(println _)
		someNumbers.foreach { println _ }

		def sum(a: Int) = {
			a
		}
		def sum2(a: Int) = a
		println(sum(5))
		println(sum2(5))

		val a = sum _
		println(a(1))
		println(a.apply(1))

		someNumbers.foreach(println)

		// closure
		var more = 2
		val addMore = (x: Int) => x + more
		println("addMore=" + addMore(10))

		//		someNumbers = List(-11, -10, -5, 0, 5, 10)
		var sum3 = 0
		someNumbers.foreach(sum3 += _)
		println("susm3:" + sum3)

		def makeIncreaser(more: Int) = (x: Int) => {
			x + more
		}
		val incl = makeIncreaser(1)
		println("incl" + incl)
		val inc9999 = makeIncreaser(9999)
		println("inc9999=" + inc9999)
		
		println(incl(10))
		println(inc9999(10))
	}
}