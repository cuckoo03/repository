package com.scala.pis.ch23

import scala.collection.immutable.BitSet
import scala.collection.immutable.Queue
import scala.collection.immutable.Stack
import scala.collection.immutable.TreeSet
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import scala.math.Ordering
import collection.JavaConversions._
import scala.collection.immutable.HashMap

/**
 * @author cuckoo03
 */
object Chapter23 {
	def main(args: Array[String]) {

		val books: List[Book] =
			List(
				Book("a", "a1", "a11"),
				Book("b", "b1", "b11"),
				Book("c", "a1", "c11"))

		def b = for (
			b <- books; a <- b.authors if a startsWith ("a")
		) yield b.title
		println(b)

		// 중복 제거
		def removeDuplicates[A](xs: List[A]): List[A] = {
			if (xs.isEmpty) xs
			else
				xs.head :: removeDuplicates(xs.tail filter (x => x != xs.head))
		}
		println((removeDuplicates(b)))

		// 두 권 이상의 책 작가 찾기
		def c = for (
			b1 <- books; b2 <- books if b1 != b2;
			a1 <- b1.authors; a2 <- b2.authors if a1 == a2
		) yield a1
		println(c)

		val val2 = books.flatMap(b1 =>
			books.withFilter(b2 =>
				b1 != b2).flatMap(b2 =>
				b1.authors.flatMap(a1 =>
					b2.authors.withFilter(a2 =>
						a1 == a2).map(a2 =>
						a1))))
		println(val2)

		val val1 = books.flatMap { b1 =>
			books.withFilter { b2 =>
				b1 != b2
			}.flatMap { b2 =>
				b1.authors.flatMap { a1 =>
					b2.authors.withFilter { a2 =>
						a1 == a2
					}.map { a2 =>
						a1
					}
				}
			}
		}
		println(val1)

		// collection
		println(List(1, 2, 3).map(_ + 1))
		println(Set(1, 2, 3).map(_ + 1))

		// traversable trait

		// iterate trait
		val xs = List(1, 2, 3, 4, 5)
		val git = xs.grouped(3)
		println(git.next())
		println(git.next())
		val sit = xs.sliding(3)
		println(sit.next())
		println(sit.next())
		println(sit.next())

		// set
		val fruit = Set("apple", "orange", "peach", "banana")
		println(fruit("peach"))
		println(fruit("potato"))

		var s = collection.immutable.Set(1, 2, 3)
		s += 4; s -= 2;
		println(s)
		val s1 = collection.mutable.Set(1, 2, 3)
		s1 += 4; s1 -= 2;
		println(s1)

		//sortedset
		val myOrdering = Ordering.fromLessThan[String](_ > _)
		val set = TreeSet.empty(myOrdering)
		val numbers = set + ("1", "2", "3") // println 3,2,1
		println(numbers)
		println(numbers.range("2", "1"))

		// map
		def f(x: String) = {
			println("taking my time")
			Thread.sleep(100)
			x.reverse
		}
		val cache = collection.mutable.Map[String, String]()
		def cachedF(s: String) = cache.getOrElseUpdate(s, f(s))
		println(cachedF("abc"))
		println(cachedF("abc"))

		val capital = MapMaker.makeMap
		capital ++= List("US" -> "Washington")
		println(capital("US"))

		// immutable collection
		// stream
		println(1 #:: 2 #:: 3 #:: Stream.empty)
		def fibFrom(a: Int, b: Int): Stream[Int] =
			a #:: fibFrom(b, a + b)
		println(fibFrom(1, 1).take(7))
		println(fibFrom(1, 1).take(7).toList)

		// vector
		val vec = Vector.empty
		val vec2 = vec :+ 1 :+ 2
		val vec3 = 100 +: vec2
		println(vec)
		println(vec2)
		println(vec3(0))

		val vec4 = Vector(1, 2, 3)
		println(vec4.updated(2, 4))
		println(vec4)

		println(IndexedSeq(1, 2, 3))

		// immutable stack
		immutablestack()
		immutableQueue()
		range()
		redblacktree()
		immutableBitSet()
		mutableArrayBuffer()
		mutableListBuffer()
		mutableStringBuilder()
		mutableQueue()

		//array
		array()

		// equallity
		//equality() // failed
		// view
		iterator()
		javaScala()
	}

	def javaScala() = {
		println("")
		val jul:java.util.List[Int] = ArrayBuffer(1,2,3)	
		println(jul)
		val buf:Seq[Int] = jul
		println(buf)
		val m:java.util.Map[String, Int] = HashMap("a"->1, "b"->2)
		println(m)
		
		// immutable list에 변경 연산을 시도하면 에러 발생
		val jul2:java.util.List[Int] = collection.immutable.List(1,2,3)
//		jul2.add(7)
	}
	
	def iterator() = {
		println("")
		val it = Iterator(1, 2, 3, 4)
		val bit = it.buffered
		println(bit.head)
		println(bit.next())
		println(bit.next())
	}

	def equality() = {
		val buf = ArrayBuffer(1, 2, 3)
		val map = collection.mutable.HashMap(buf -> 3)
		println(map(buf))
		println(buf(0) += 1)
		println(map(buf))

	}

	def array() = {
		val a1 = Array(1, 2, 3)
		val a2 = a1.map(_ * 3)
		println(a2)
		val a3 = a2.filter(_ % 2 != 0)
		println(a3)
	}

	def mutableQueue() = {
		println()
		val queue = collection.mutable.Queue.empty[String]
		println(queue += "a")
		println(queue ++= List("b", "c"))
		println(queue.dequeue())
		println(queue)
	}
	def mutableStringBuilder() = {
		println()
		val buf = new StringBuilder
		println(buf += 'a')
		println(buf ++= "bc")
		println(buf.toString())
	}
	def mutableListBuffer() = {
		println()
		val buf = ListBuffer.empty[Int]
		println(buf += 1)
		println(buf += 10)
		println(buf.toList)
	}
	def mutableArrayBuffer() = {
		println()
		val buf = ArrayBuffer.empty[Int]
		println(buf += 1)
		println(buf += 10)
		println(buf.toArray)
	}

	def immutableBitSet() = {
		val bits = BitSet.empty
		val moreBits = bits + 3 + 4 + 4
		println(moreBits(3))
		println(moreBits(0))
	}

	def redblacktree() = {
		val set = TreeSet.empty[Int]
		println(set + 1 + 3 + 3)
	}

	def range() = {
		println(1 to 3)
		println(5 to 14 by 3)
		println(1.until(3))
	}
	def immutableQueue() = {
		println()
		val empty = Queue[Int]()
		val has1 = empty.enqueue(1)
		println(has1)
		val has123 = has1.enqueue(List(2, 3))
		println(has123)
		println(has123.dequeue)
	}

	def immutablestack() = {
		println("")
		val stack = Stack.empty
		val hasOne = stack.push(1)
		println(hasOne)
		println(hasOne.top)
		println(hasOne.pop)
	}
}
case class Book(title: String, authors: String*)