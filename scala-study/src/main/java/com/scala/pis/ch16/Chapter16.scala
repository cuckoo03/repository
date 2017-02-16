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
		println(for (i <- List.range(1, 5); j <- List.range(1, i)) yield (i, j))

		// foreach
		var sum = 0
		List(1, 2, 3, 4, 5) foreach (sum += _)
		println("sum:" + sum)

		//filter
		println(List(1, 2, 3, 4, 5) filter (_ % 2 == 0))

		//partition
		println(List(1, 2, 3, 4, 5) partition { x => x % 2 == 0 })
		println(List(1, 2, 3, 4, 5) partition (_ % 2 == 0))

		//find
		println(List(1, 2, 3, 4, 5) find (_ % 2 == 0))
		println(List(1, 2, 3, 4, 5) find (_ <= 0))

		// takeWhile
		println(List(1, 2, 3, -4, 5) takeWhile (_ > 0))

		// dropWhile
		println(List(1, 2, 3, -4, 5) dropWhile (_ > 0))

		// span
		println(List(1, 2, 3, -4, 5) span (_ > 0))

		//forall
		println(List(1, 2, 3, 4, 5) forall (_ > 0))
		println(List(1, 2, 3, -4, 5) forall (_ > 0))

		// exists
		println(List(1, 2, 3, 4, 5) exists (_ > 0))
		println(List(1, 2, 3, -4, 5) exists (_ > 0))

		// fold: /: :\

		// sortWith
		println(List(1, -3, 4, 2, 6) sortWith (_ < _))

		// List.range
		println(List.range(1, 5))
		println(List.range(1, 9, 2))
		println(List.range(9, 1, -3))

		//List.fill
		println(List.fill(5)('a'))
		println(List.fill(2, 3)('b'))

		//List.tabulate
		println(List.tabulate(5)(n => n * n))
		println(List.tabulate(5, 5)(_ * _))

		//List.concat
		println(List.concat(List('a', 'b'), List('c')))

		println((List(10, 20), List(3, 4, 5)).zipped.map(_ * _))
	}
}