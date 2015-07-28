package com.scala.pis.ch16

/**
 * @author cuckoo03
 */
object Chapter16 {
	def main(args: Array[String]) {
		val a = List()
		val b = List("a")
		val fruit: List[String] = List("a", "b", "c")
		val nums: List[Int] = List(1, 2, 3)
		val diag3: List[List[Int]] = List(List(1), List(2))
		val empty: List[Nothing] = List()
		println(fruit)
		println(nums)
		println(diag3)
		println(empty)
		println("")

		val fruit2 = "a" :: ("b" :: ("c" :: Nil))
		val nums2 = "a" :: "b" :: "c" :: Nil
		println(fruit2)
		println(nums2)

		println(fruit2.head)
		println(fruit2.tail)
		println(fruit2.isEmpty)
		println(fruit2.tail.head)
		println("")

		var count: Int = 0
		def issort(xs: List[Int]): List[Int] =
			if (xs.isEmpty) {

				Nil
			} else {
				count = count + 1
				println("" + xs.head :: xs.tail)
				insert(xs.head, issort(xs.tail))
			}

		def insert(x: Int, xs: List[Int]): List[Int] = {
			println(":" + x :: xs)

			if (xs.isEmpty || x <= xs.head) {
				println("::" + x :: xs)
				x :: xs
			} else {
				println(":::" + x :: xs)
				xs.head :: insert(x, xs.tail)
			}
		}

		println(issort(List(1, 2, 3, 4)))
		println("")

		println(List(1, 2) ::: (List(3, 4)))
		println(List() ::: List(1, 2))
		println("")

		println(List(1, 2, 3).length)
		println(List(1, 2, 3).last)
		println(List(1, 2, 3).init)

		println("reverse" + List(1, 2, 3).reverse)
		println("take" + List(1, 2, 3, 4).take(2))
		println("drop" + List(1, 2, 3, 4).drop(2))
		println("splitAt" + List(1, 2, 3, 4).splitAt(1))

		println("apply" + List(1, 2, 3, 4).apply(2)) // start 0
		println("indices" + List(1, 2, 3, 4).indices)
		println("flatten" + List(List(1), List(2)).flatten)
		println("zip" + (List(1, 2) zip (List(3, 4, 5))))
		println("tostring" + List(1, 2).toString())
		println("mkstring" + List(1, 2).mkString("[", ":", "]"))

		println("toArray" + "abc".toArray)
		println("toList" + "abc".toList)

		val arr = new Array[Int](10)
		List(1, 2, 3).copyToArray(arr, 2)
		println("copyToArray" + arr)

		println(List(1, 2, 3).iterator.next())

		// map
		println(List(1, 2, 3) map (_ + 1))
		val words = List("a", "b", "c", "d")
		println(words map (_.length()))
		println(words map (_.toList.reverse.mkString))

		// flatmap
		// foreach
	}
}