package com.scala.pis.ch03

import java.math.BigInteger
import scala.collection.mutable.Set
import scala.collection.mutable.Map
import scala.io.Source

object Exam01 extends App {
	var big = new BigInteger("12345")

	val greet = new Array[String](3)
	greet(0) = "hello"
	greet.update(0, "hello1")
	for (i <- 0 to 0) {
		println(greet.apply(i))
	}
	var greet1: Array[String] = new Array[String](3)

	val numNames = Array.apply("1", "2", "3")
	for (i <- 0 to 2)
		println(numNames(i))

	val oneTwo = List(1, 2)
	val three = List(3)
	val onetwothree = oneTwo ::: three
	println(onetwothree)
	val onetwothree2 = 1 :: three
	println(onetwothree2)

	val onetwothree3 = 1 :: 2 :: 3 :: Nil
	println(onetwothree3)

	// tuple
	val pair = (3, "a")
	println(pair._1)
	println(pair._2)

	// set
	var set = Set("a", "b")
	set += "c"
	println(set.contains("c"))

	val set2 = Set("a", "b")
	set2 += "c"
	println(set2.contains("c"))

	// map
	val map = Map[Int, String]()
	map += (1 -> "a")
	println(map(1))

	val map2 = Map(1 -> "a")
	println(map2(1))

	for (line <- Source.fromFile("""c:\Users\cuckoo03\git\repository\scala-study\src\main\java\com\scala\pis\ch03\Exam01.scala""").getLines()) {
		println(line.length() + " " + line)
	}
	
	

}