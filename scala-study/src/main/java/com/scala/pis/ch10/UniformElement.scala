package com.scala.pis.ch10

/**
 * @author cuckoo03
 */
class UniformElement(ch: Char, override val width: Int,
	override val height: Int) extends Element {
	private val line = ch.toString() * width
	def contents = Array.fill(height)(line)
}