package com.scala.pis.ch10

/**
 * @author cuckoo03
 */
abstract class Element {
	def contents: Array[String]
	def height: Int = contents.length
	def width: Int = if (height == 0) 0 else contents(0).length()

	def above(that: Element): Element =
		new ArrayElement(this.contents ++ that.contents)

	def besisde(that: Element): Element = {
		new ArrayElement(
			for ((line1, line2) <- this.contents zip that.contents)
				yield line1 + line2)
		
		// 작성미완료
//		override def toString = contents mkSTring "\n"
	}
}