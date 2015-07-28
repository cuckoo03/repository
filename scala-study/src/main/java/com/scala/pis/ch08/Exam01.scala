package com.scala.pis.ch08

import java.io.File

/**
 * @author cuckoo03
 */
object Exam01 {
	def main(args: Array[String]) {
		//		val width = args(0).toInt
		//		for (arg <- args.drop(1))
		//			LongLine.processFile(arg, width)

		// first class function
		var increase = (x: Int) => x + 1
		println(increase(1))

		increase = (x: Int) => {
			println("a")
			x - 1
		}
		println(increase(1))

		val someNumbers = List(-10, 10)
		someNumbers.foreach { x => println(x) }
		someNumbers.foreach { println _ }

		println(someNumbers.filter((x: Int) => x > 0))
		println(someNumbers.filter(x => x > 0))

		println(someNumbers.filter(_ > 0))
		
		

	}
}