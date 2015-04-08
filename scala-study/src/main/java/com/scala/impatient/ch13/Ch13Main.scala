package com.scala.impatient.ch13

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.LinkedList
import scala.collection.mutable.Map
import scala.collection.JavaConversions.propertiesAsScalaMap

object Ch13Main extends App {
	var digits = List(4, 2)
	println(digits)
	var digits2 = 9 :: List(4, 2)
	println(digits2)

	def sum(list: List[Int]): Int =
		if (list == Nil) {
			0
		} else {
			list.head + sum(list.tail)
		}

	println(digits.sum)
	println(digits2.sum)

	val list = LinkedList(1, -2, 7, -9)
	var cur = list
	while (cur != Nil) {
		if (cur.elem < 0) {
			cur.elem = 0
		}
		cur = cur.next
	}
	println(list)

	cur = list
	while (cur != Nil && cur.next != Nil) {
		cur.next = cur.next.next
		cur = cur.next
	}
	println(list)

	//set
	println(Set(2, 0, 1) + 1)
	var iter = Set(1, 2, 3, 4).iterator
	while (iter.hasNext) {
		print(iter.next + ",")
	}
	println("")

	//add remove
	println(Vector(1, 2, 3) :+ 5)
	println(1 +: Vector(1, 2, 3))
	val numbers = ArrayBuffer(1, 2, 3)
	numbers += 5
	println(numbers)

	var numbers2 = Set(1, 2, 3)
	numbers2 += 5
	var numberVector = Vector(1, 2, 3)
	numberVector :+= 5

	println(Set(1, 2, 3) - 2)
	//common method

	// function mapping
	val names = List("a", "b", "c")
	println(names.map(_.toUpperCase))
	println(names)
	names.foreach(println)

	//reduce
	val rf = List(1, 7, 2, 9).reduceLeft(_ - _)
	println(rf)
	var lf = List(1, 7, 2, 9).reduceRight(_ - _)
	println(lf)
	println(List(1, 7, 2, 9).foldLeft(0)(_ - _))

	//zipping

	//iterator

	//stream

	//rageview

	// javaconversions
	var props: scala.collection.mutable.Map[String, String] = System.getProperties()
	props("scala") = "a"
	println(props.get("scala"))
	
	// threadsafe collection
	
	// parallel collection
}