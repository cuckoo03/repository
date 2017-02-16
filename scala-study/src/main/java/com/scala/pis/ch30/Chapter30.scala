package com.scala.pis.ch30

/**
 * @author cuckoo03
 */
object Chapter30 {
	def main(args: Array[String]) {
		val p1, p2 = new Point(1, 2)
		val q = new Point(2, 3)
		println(p1.equals(p2))
		println(p1.equals(q))
		
		val col1 = collection.mutable.HashSet(p1)
		println(col1.contains(p2))
		
		val p2a:Point = p2
		println(p2a)
	}

	class Point(val x: Int, val y: Int) {
		override def hashCode = 41 * (41 + x) + y
		override def equals(other:Any) = other match {
			case that:Point => this.x == that.x && this.y == that.y
			case _=> false
		}
//		def equals(other: Point): Boolean = this.x == other.x && this.y == other.y
	}
}