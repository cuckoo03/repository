package com.fpinscala.ch02

/**
 * @author cuckoo03
 */
object MyModule {
	def main(args: Array[String]): Unit = {
		println(factorial(3))
		println(findFirst(Array(7, 9, 13), (x: Int) => x == 9))
		println(isSorted(Array(7, 9, 13), (x: Int, y: Int) => x == y))
	}

	def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
		false
	}

	def findFirst[A](as: Array[A], p: A => Boolean): Int = {
		def loop(n: Int): Int =
			if (n >= as.length) -1
			else if (p(as(n))) n
			else loop(n + 1)

		loop(0)
	}

	private def formatFactorial(n: Int) = {
		val msg = "factorial of %d is %d."
		msg.format(n, factorial(n))
	}

	def factorial(n: Int): Int = {
		def go(n: Int, acc: Int): Int = {
			if (n <= 0) acc
			else go(n - 1, n * acc)
		}

		go(n, 1)
	}

}