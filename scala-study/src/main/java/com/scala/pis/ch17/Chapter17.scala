package com.scala.pis.ch17

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.TreeSet

/**
 * @author cuckoo03
 */
object Chap17 {
	def main(args: Array[String]) {
		// sequence:순서가 정해진 데이터 그룹에 작업 가능
		// list
		println(List(1, 2, 3).head)
		println(List(1, 2, 3).tail)

		// array
		println(new Array[Int](5))
		println(Array(0))
		println(Array(1, 2, 3, 4, 5))

		// listbuffer
		val buf = new ListBuffer[Int]
		println(buf += 1)
		println(buf += 2)
		println(3 +=: buf)

		// arraybuffer
		val buf2 = new ArrayBuffer[Int]()
		println(buf2 += 12)
		println(buf2 += 15)
		println(buf2.length)
		println(buf2(0))

		// stringops
		def hasUpperCase(s: String) = s.exists(_.isUpper)
		println(hasUpperCase("Robert"))
		println(hasUpperCase("e"))

		// set, map
		val text = "See Spot run."
		val wordsArray = text.split("] !,.]+")
		println(wordsArray.toString())

		var map = scala.collection.mutable.Map.empty[String, Int]
		println(map)
		map("hello") = 1

		def countWord(text: String) = {
			val counts = scala.collection.mutable.Map.empty[String, Int]
			for (rawWord <- text.split("[ ,!.]+")) {
				val word = rawWord.toLowerCase()
				val oldCount =
					if (counts.contains(word))
						counts(word)
					else 0
				counts += (word -> (oldCount + 1))
			}
			counts
		}

		println(countWord("See Spot run Run, Spot. Run"))

		// sorted set, map
		println(TreeSet(1, 3, 4, 2))
		println(TreeSet("b", "a", "c"))

		//
		var people = Set("a", "b")
		people += "c"
		println(people)
		people -= "b"
		println(people)
		people ++= List("d", "e")
		println(people)

		// change mutable to immutable map
		var muta = scala.collection.mutable.Map("i" -> 1)
		val immu = Map.empty ++ muta
		println(immu)

		// tuple

		def longestWord(words: Array[String]) = {
			var word = words(0)
			var idx = 0
			for (i <- 1 until words.length)
				if (words(i).length() > word.length()) {
					word = words(i)
					idx = i
				}
			(word, idx)
		}
		val logest = longestWord("The quick brown fox".split(" "))
		println(logest)
		println(logest._1)
		println(logest._2)
		val (word, idx) = logest
		println(word + ", " + idx)
		
		
	}
}
