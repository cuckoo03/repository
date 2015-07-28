package com.scala.pis.ch10

/**
 * @author cuckoo03
 */
class LineElement(s: String) extends Element {
	val contents = Array(s)
	override def width = s.length()
	override def height = 1
}