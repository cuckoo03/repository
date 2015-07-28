package com.scala.pis.ch10

/**
 * @author cuckoo03
 */
object Exam01 {
	def main(args: Array[String]) {
		def e: Element = new ArrayElement(Array("hello", "world"))
		println(e)
		println(e.width)
		println(e.height)

		val ae: Element = new LineElement("hello")
		val e2: Element = ae
		val e3: Element = new UniformElement('x', 2, 3)
		
	}
}